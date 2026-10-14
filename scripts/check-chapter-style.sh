#!/usr/bin/env bash
# Checks the chapter conventions from CONTRIBUTING.md that are invisible in
# any single file and therefore decay quietly:
#   1. No heading deeper than ## inside a chapter.
#   2. No Markdown tables inside a chapter -- they belong in README and
#      PROJECT-IDEAS. Chapters use fenced example-output blocks instead.
#   3. Every ## heading is one of the agreed shapes, in German or English.
#
# Course 1's Chapter 0 is exempt from rule 3: it introduces the green/yellow/
# red system rather than using it, so its headings are plain and descriptive.
set -uo pipefail
shopt -s nullglob globstar

fail=0

chapters=(courses/**/*.md)
if [ ${#chapters[@]} -eq 0 ]; then
  echo "No chapters yet."
  exit 0
fi

for f in "${chapters[@]}"; do
  while IFS=: read -r line _; do
    [ -n "$line" ] || continue
    echo "::error file=$f,line=$line::heading deeper than ## -- chapters use # once and ## for everything else"
    fail=1
  done < <(grep -n '^###' "$f" | cut -d: -f1)

  while IFS=: read -r line _; do
    [ -n "$line" ] || continue
    echo "::error file=$f,line=$line::Markdown table in a chapter -- use a fenced example-output block instead"
    fail=1
  done < <(grep -n '^|' "$f" | cut -d: -f1)

  case "$f" in
    courses/01-basics/*/00-*) continue ;;
  esac

  while IFS= read -r entry; do
    [ -n "$entry" ] || continue
    line="${entry%%:*}"
    text="${entry#*:}"
    case "$text" in
      '## 🟢 Core — '*|'## 🟢 Kern — '*) ;;
      '## 🟢 Core exercise — '*|'## 🟢 Kernübung — '*) ;;
      '## 🟡 Optional — '*) ;;
      '## 🔴 Optional, genuine challenge — '*|'## 🔴 Optional, echte Herausforderung — '*) ;;
      '## What you learned'|'## Was du gelernt hast') ;;
      '## Next'|'## Weiter'|"## What's next"|'## Was als Nächstes kommt') ;;
      *)
        echo "::error file=$f,line=$line::unexpected ## heading: $text"
        fail=1
        ;;
    esac
  done < <(grep -n '^## ' "$f")

  # A code fence that carries text after it. An opening fence may name a
  # language, a closing one may not -- CommonMark then does not treat it as a
  # close, and the rest of the chapter silently renders as code. This went
  # unnoticed in Course 5 in both languages until somebody read the raw file.
  while IFS=: read -r line text; do
    case "$text" in
      '```'|'```java'|'```text') ;;
      *)
        echo "::error file=$f,line=$line::code fence with trailing text: $text"
        fail=1
        ;;
    esac
  done < <(grep -n '^```' "$f")

  # An odd number of fences means one block never closes.
  fences=$(grep -c '^```' "$f")
  if [ $((fences % 2)) -ne 0 ]; then
    echo "::error file=$f::odd number of code fences ($fences); one block is unclosed"
    fail=1
  fi
done

if [ "$fail" -eq 0 ]; then
  echo "Chapter style check passed."
fi

exit "$fail"
