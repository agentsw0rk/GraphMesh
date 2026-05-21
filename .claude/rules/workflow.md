---
name: Workflow & DoD
scope: docs
layer: workflow
priority: medium
appliesTo: all
description: Working mode, planning, git & commit conventions, and Definition of Done.
---

# Workflow & Definition of Done Rules

- New features & code research:
    - Use the superpowers skills for new features, and research code examples via context7.
- Planning:
    - For multi-step tasks, first write a short bullet-point plan.
    - For unclear requirements: first extend/clarify the relevant feature spec in `docs/features/`.
- Definition of Done:
    - Feature spec updated or newly created.
    - Implementation in the matching feature package (Modulith rules respected).
    - Relevant tests (unit/integration) created/updated and green.
    - For a complete feature: `*-done.md` created and `00-feature-set-overview.md` updated.
- Git & Commits:
    - Commit directly to `main`; no PRs.
    - **Never** run `git push`, `git reset --hard`, `--force`, branch deletes, or `--no-verify` without explicit instruction.
- Learning:
    - Record important recurring lessons about working with you in a doc (e.g. `docs/lessons/claude-lessons.md`).
