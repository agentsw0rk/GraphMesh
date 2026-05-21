---
name: Feature Specs & Docs
scope: docs
layer: workflow
priority: medium
appliesTo: all
paths:
  - "docs/features/**"
  - "docs/superpowers/**"
description: Rules for feature specs, completion docs, overviews, doc locations, and language.
---

# Feature Specs & Docs Rules

- Every relevant functional change needs a feature spec in `docs/features/`.
- Write the feature doc BEFORE implementing code.
- Naming scheme (kebab-case):
    - `NN-feature-name.md` = specification
    - `NN-feature-name-done.md` = completion documentation
    - `NN` = two-digit sort number (01, 02, 03, …)
- Update `docs/features/00-feature-set-overview.md` when:
    - a new feature is added
    - a feature is completed (status "done")
- For unclear task descriptions:
    - First propose changes to the relevant feature spec before changing code.
- Completion docs (`*-done.md`) describe (see examples like `46-skos-taxonomy-done.md`, `49-markdown-document-support-done.md`):
    - What was implemented?
    - Relevant APIs/models
    - Optionally screenshots / example calls
- Other doc locations:
    - Design specs: `docs/superpowers/specs/YYYY-MM-DD-<topic>-design.md`
    - Implementation plans: `docs/superpowers/plans/YYYY-MM-DD-<feature-name>.md`
- Language:
    - Docs and commit messages may be German; avoid umlauts (write ae/oe/ue/ss).
    - Agent-instruction files (`CLAUDE.md`, `.claude/rules/*.md`) are written in English.
