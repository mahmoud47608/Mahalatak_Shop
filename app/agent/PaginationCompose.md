# Helol → Compose Pagination Mapping

## Paging Source
| Helol | Target Compose |
|--------|---------------|
| NotificationsPagingSource | Feature-specific PagingSource |
| load() implementation | Consistent page loading logic |
| next/prev keys | Stable page key strategy |

---

## Pagination Helper
| Helol | Target Compose |
|--------|---------------|
| PaginationHelper | Reusable pagination utility |
| Load state handling | Unified UI state |
| Retry support | Retry trigger from UI/ViewModel |

---

## ViewModel Integration
| Helol | Target Compose |
|--------|---------------|
| Pager configuration | Pager aligned with project architecture |
| Flow<PagingData> | Lifecycle-aware Flow |
| Cached data | cachedIn(viewModelScope) |

---

## UI Layer
| Helol | Target Compose |
|--------|---------------|
| Paging with lists | LazyColumn / LazyGrid |
| Load indicators | Compose loading items |
| Error item | Retry UI component |

---

## State Management
| Helol | Target Compose |
|--------|---------------|
| Combined load states | Derived UI state |
| PagingData stream | Collected as LazyPagingItems |
| Manual checks | Declarative UI updates |

---

## Architecture
| Helol | Target Compose |
|--------|---------------|
| Feature-coupled paging | Generic, reusable pagination |
| Utility classes | Modular components |
| Direct usage | Adapted to match target architecture |
