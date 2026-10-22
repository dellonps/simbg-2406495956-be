# Git workflow

Backend and frontend are maintained as separate Git repositories. Run Git
commands from the relevant project directory.

## Before and after changing code

```sh
git status
git diff
```

Create a branch for each feature or fix instead of working directly on `main`:

```sh
git switch -c feat/short-description
```

Stage only the files related to the change, review the staged diff, then commit
with a short, descriptive message:

```sh
git add <files>
git diff --cached
git commit -m "feat: describe the change"
```

Use `fix:`, `docs:`, or another clear prefix when it better describes the work.
When a remote is configured, push the branch and open a pull request for review.
Keep generated files, local configuration, and secrets out of commits; the
repository's `.gitignore` excludes these files.

## Reviewing history

```sh
git log --oneline --graph --decorate
```
