# README - Memory Bank Documentation

## Overview

This memory bank contains comprehensive documentation of the PayPal Calc Android application, designed to help developers, AI agents, and maintainers quickly understand and work with the codebase.

## Document Structure

### 1. [project-overview.md](project-overview.md)
**What it covers**: High-level understanding of the app
- App purpose and functionality
- Project identity (package name, version, Play Store link)
- Technical stack overview
- Feature list and supported currencies
- Development history and monetization strategy
- Key design decisions

**Use when**: 
- First time working on the project
- Explaining the app to new team members
- Writing documentation or proposals
- Understanding business context

### 2. [architecture.md](architecture.md)
**What it covers**: Code structure and design patterns
- MVP (Model-View-Presenter) pattern implementation
- Layer responsibilities (Models, Views, Presenters, Providers, Managers, Adapters)
- Data flow through the application
- Singleton pattern usage
- Custom components (ChangeDialog, PayPalCalcApplication)
- Integration between components

**Use when**:
- Adding new features
- Refactoring code
- Understanding component interactions
- Debugging data flow issues
- Planning architectural changes

### 3. [build-and-deployment.md](build-and-deployment.md)
**What it covers**: Building, configuring, and deploying the app
- Gradle configuration and build commands
- Dependencies and versions
- ProGuard setup (currently disabled)
- Signing configuration and keystore details
- Required configuration files (google-services.json)
- Play Store deployment process
- Common build issues and solutions

**Use when**:
- Setting up development environment
- Building release APKs
- Troubleshooting build errors
- Updating dependencies
- Preparing for Play Store release
- Configuring CI/CD

### 4. [code-patterns.md](code-patterns.md)
**What it covers**: Coding standards and conventions
- Design patterns (Singleton, MVP, Template Method, Static Factory, Observer)
- Naming conventions (variables, classes, methods, resources)
- Code organization and file structure
- Common implementation patterns
- View binding with ButterKnife
- Presenter binding flow
- Error handling approach
- Code style guidelines

**Use when**:
- Writing new code
- Code review
- Maintaining consistency
- Onboarding new developers
- Refactoring existing code

### 5. [ui-documentation.md](ui-documentation.md)
**What it covers**: User interface design and implementation
- Screen layout structure
- UI components (Spinner, EditText, CardViews, Dialogs)
- Material Design elements
- User interactions and flows
- Responsive design approach
- AdMob banner integration
- Accessibility considerations
- Edge cases and error states

**Use when**:
- Modifying UI layouts
- Adding new UI features
- Fixing UI bugs
- Understanding user flows
- Implementing responsive design
- Accessibility improvements

### 6. [data-and-logic.md](data-and-logic.md)
**What it covers**: Data models and business calculations
- Currency data model (properties, Parcelable implementation)
- PayPal fee calculation formula with mathematical derivation
- Worked examples for different currencies
- Currency data structure (26 predefined currencies)
- Data persistence mechanism
- State management in Presenter
- Formatting logic
- Validation approach
- Calculation performance considerations

**Use when**:
- Understanding the core calculation
- Adding new currencies
- Modifying fee structures
- Debugging calculation errors
- Implementing data validation
- Optimizing performance
- Writing unit tests

### 7. [external-dependencies.md](external-dependencies.md)
**What it covers**: Third-party integrations and libraries
- Google Play Services (AdMob, Analytics)
- ButterKnife view injection
- Gson serialization
- Android Support Libraries
- Android permissions
- Play Store integration
- Security considerations
- Testing external dependencies
- Monitoring and dashboards

**Use when**:
- Updating dependencies
- Troubleshooting integration issues
- Setting up API keys
- Configuring AdMob or Analytics
- Understanding permission requirements
- Migrating to newer libraries
- Monitoring app performance

## Quick Reference

### File Locations
```
PayCalc/
├── app/src/main/java/com/walng/dhagz/paypalcalc/
│   ├── models/              → See: data-and-logic.md
│   ├── views/               → See: architecture.md, ui-documentation.md
│   ├── presenters/          → See: architecture.md, data-and-logic.md
│   ├── providers/           → See: architecture.md, data-and-logic.md
│   ├── managers/            → See: architecture.md, external-dependencies.md
│   ├── adapters/            → See: architecture.md, ui-documentation.md
│   ├── MainActivity.java    → See: architecture.md, ui-documentation.md
│   └── ChangeDialog.java    → See: architecture.md, ui-documentation.md
├── app/src/main/res/
│   ├── layout/              → See: ui-documentation.md
│   └── values/              → See: ui-documentation.md
├── app/build.gradle         → See: build-and-deployment.md, external-dependencies.md
└── .github/
    └── copilot-instructions.md  → Quick reference for AI agents
```

### Common Tasks

**Build Release APK**:
```powershell
.\gradlew assembleRelease
```
See: [build-and-deployment.md](build-and-deployment.md#build-commands)

**Understand Calculation**:
See: [data-and-logic.md](data-and-logic.md#the-paypal-formula)

**Add New Currency**:
1. See: [data-and-logic.md](data-and-logic.md#default-currencies-26-total)
2. Edit: `CurrencyListProvider.getDefaultCurrencyList()`
3. Add: `new Currency(id, name, symbol, percent, fixed)`

**Modify UI Layout**:
1. See: [ui-documentation.md](ui-documentation.md#layout-structure)
2. Edit: `app/src/main/res/layout/activity_main.xml`
3. Update: ButterKnife bindings in `MainActivity.java`

**Update AdMob Settings**:
1. See: [external-dependencies.md](external-dependencies.md#admob-integration)
2. Edit: `app/src/main/res/values/donottranslate.xml`
3. Update: `banner_ad_unit_id`

**Implement New Feature**:
1. Review: [architecture.md](architecture.md) for MVP pattern
2. Review: [code-patterns.md](code-patterns.md) for conventions
3. Add Model (if needed): [data-and-logic.md](data-and-logic.md)
4. Add View interface method: [architecture.md](architecture.md#views-views)
5. Add Presenter logic: [architecture.md](architecture.md#presenters-presenters)
6. Update Activity: [architecture.md](architecture.md#activity-layer)
7. Update UI: [ui-documentation.md](ui-documentation.md)

### Key Concepts

**MVP Pattern**: [architecture.md](architecture.md#architectural-pattern-mvp-model-view-presenter)
**Singleton Pattern**: [code-patterns.md](code-patterns.md#1-singleton-pattern-critical)
**PayPal Formula**: [data-and-logic.md](data-and-logic.md#the-paypal-formula)
**ButterKnife Binding**: [code-patterns.md](code-patterns.md#1-view-binding-with-butterknife)
**Data Persistence**: [architecture.md](architecture.md#providers-providers)

## Document Usage Guidelines

### For New Developers
**Start here**:
1. [project-overview.md](project-overview.md) - Understand what the app does
2. [architecture.md](architecture.md) - Learn the code structure
3. [code-patterns.md](code-patterns.md) - Follow coding standards
4. [build-and-deployment.md](build-and-deployment.md) - Set up environment

### For AI Agents
**Refer to**:
- `.github/copilot-instructions.md` for quick reference
- Specific memory bank docs for detailed context
- Cross-reference between docs for complete understanding

### For Maintenance
**Focus on**:
1. [build-and-deployment.md](build-and-deployment.md) - Update dependencies
2. [external-dependencies.md](external-dependencies.md) - Update integrations
3. [data-and-logic.md](data-and-logic.md) - Update currency fees

### For Feature Development
**Review**:
1. [architecture.md](architecture.md) - Follow existing patterns
2. [code-patterns.md](code-patterns.md) - Maintain consistency
3. [ui-documentation.md](ui-documentation.md) - Integrate with UI
4. [data-and-logic.md](data-and-logic.md) - Understand data flow

## Maintenance

### Updating Documentation
When making significant code changes:
1. Update affected memory bank document(s)
2. Update `.github/copilot-instructions.md` if patterns change
3. Add examples for new patterns
4. Document breaking changes
5. Update this README if structure changes

### Document Freshness
**Created**: January 2026
**Based on Code**: Version 1.4 (versionCode 5)
**Last Updated**: Initial creation

## Additional Resources

### External Links
- **Play Store**: https://play.google.com/store/apps/details?id=com.walng.dhagz.paypalcalc
- **PayPal Fees**: https://www.paypal.com/ph/webapps/mpp/merchant-fees
- **Android Docs**: https://developer.android.com/
- **ButterKnife**: https://jakewharton.github.io/butterknife/

### Related Files
- `.github/copilot-instructions.md` - AI agent quick reference
- `README.md` - Project README (basic info, keystore details)
- `app/build.gradle` - Dependencies and versions
- `proguard-rules.pro` - ProGuard configuration

## Contributing to Documentation

When adding to memory bank:
1. **Be Specific**: Include code examples, file paths, line numbers
2. **Cross-Reference**: Link to related sections in other docs
3. **Explain Why**: Don't just document what, explain why it's done this way
4. **Keep Updated**: Update when implementation changes
5. **Use Examples**: Real code snippets are better than abstract descriptions

## Questions or Gaps?

If documentation is unclear or incomplete:
1. Check the actual source code files
2. Cross-reference multiple memory bank documents
3. Look for `// comments` in the code
4. Check commit history for context
5. Update documentation with your findings

---

**Note**: This memory bank is designed to be comprehensive but not exhaustive. For implementation details, always refer to the actual source code files. Documentation captures patterns, decisions, and context that may not be obvious from code alone.
