# Contributing
Thank you for wanting to contribute to the NST Suite of tools. This page contains instructions and guidelines for getting involved.

## Table of Contents
- [Contributing](#contributing)
- [Table of Contents](#table-of-contents)
- [System Requirements](#system-requirements)
- [Contribution Steps](#contribution-steps)
- [Additional Info](#additional-info)

## System Requirements
- JDK 8 or later
- Maven

## Contribution Steps

Before writing any code, please submit a new issue to [GitHub](https://github.com/eBay/nstsuite/issues). If you wish to work on an existing issue, please request to do so on the relevant ticket.

Contributions **MUST** be submitted via pull requests (PRs) to the main branch and **MUST** include the issue number they address. PRs **MUST** be passing all static analysis checks and tests before they will be reviewed.

Here is a basic overview of steps to perform when contributing to this project:

- GitHub team members **MUST** create a new branch in the NSTSuite repo. Non-team members will need to create their own fork and create a new branch for their changes.
- Make your changes and push them to your branch
- All PRs **MUST** include tests
- **DO NOT INCREMENT THE VERSION NUMBER. PROJECT OWNERS WILL MANAGE THIS.**
- Rebase your branch before creating a PR
- Submit a PR to the repositories main branch (confirm all PR checks are passing)
	- PR title must include the issue number and title
	- PR description must contain a comment summarizing the contents of the PR
- Update the GitHub issue with the generated PR link

## Additional Info
Please review the respective module READMEs when contributing. They will include specific details for compiling/working with the project. NSTest and TMBuilder are two separate projects. NSTest is published to Maven Central. The TMBuilder is downloadable as a release artifact in the repo. Please reach out to a project owner if you need assistance or desire a release build for a specific change.
