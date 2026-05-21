---
name: Frontend Standards
scope: frontend
layer: coding
priority: medium
appliesTo: nextjs
paths:
  - "frontend/**"
description: Structure, data access, and styling rules for the Next.js frontend.
---

# Frontend Rules (Next.js 14)

- Structure:
    - Use the App Router (`frontend/src/app/...`) for new pages/routes.
    - Reusable UI building blocks under `frontend/src/components/...`.
    - Helpers, hooks, API clients under `frontend/src/lib/...` or analogous to the existing structure.
- Data access:
    - GraphQL access goes through a central Apollo client instance.
    - No direct `fetch` calls to GraphQL endpoints.
- Styling:
    - Tailwind for layout & utilities.
    - shadcn/ui as the base for standard components.
    - New components follow existing patterns (props, structure, styling).
