# Traincraft Versioning Plan

## Version Scheme
Using **SemVer** with pre-release versioning: `MAJOR.MINOR.PATCH[-PRERELEASE]`

## Versioning Line

| Version | Minecraft | Status | Notes |
|---------|-----------|--------|-------|
| 4.4.1 | 1.7.10 | Legacy | Last 1.7.10 release |
| 4.4.1_022 | 1.12.2 | Legacy | Last 1.12.2 release |
| **5.0.0-alpha1** | **1.20.4** | **Planned** | First NeoForge release |
| **5.1.0-alpha1** | **1.21.11** | **Planned** | Second release |

## Release Plan

### Phase 1: Alpha (5.0.0-alpha1 through 5.0.0-alphaN)
- Target: 1.20.4 LTS
- Focus: Core functionality (trains, tracks, basic rolling stock)
- No compatibility guarantees
- Skip features that require heavy API changes

### Phase 2: Alpha (5.1.0-alpha1+)
- Target: 1.21.11 (latest)
- Leverage updated NeoForge APIs
- Performance improvements
- Better compatibility with 1.21 mods

### Phase 2: Beta (5.0.0-beta1+)
- Feature freeze
- Focus on stability and bug fixes
- Add back non-essential features

### Phase 3: Stable (5.0.0)
- Full feature parity with 4.4.1_022 (where possible)
- Production ready

## Dependencies
- NeoForge 20.4.x (for 1.20.4)
- JEI (Just Enough Items) - separate release for compatibility

## Distribution
- GitHub Releases
- CurseForge (via API or manual upload)
- Modrinth (optional)

## Build Artifacts
```
Traincraft-5.0.0-alpha1-1.20.4.jar
```