# TrendBazzar

An Android product catalogue in Kotlin, built on the [DummyJSON](https://dummyjson.com/) REST API. It loads product categories, shows the products in each category in a grid, and keeps loading and error states explicit.

Built with **Kotlin · MVVM · Retrofit · Coroutines · Hilt · RecyclerView · Glide**.

---

## What it does

- **Categories** — the category list is fetched from the API and shown as a horizontal selector.
- **Products** — choosing a category loads its products into a grid with images, titles and prices.
- **Explicit states** — every request goes through a small `MyResult` wrapper, so loading, success and error are handled in the UI instead of being guessed at.
- **Splash screen** — a short branded start screen before the catalogue.

## Architecture

MVVM with a single repository in front of the API and Hilt supplying it.

```
ui/            MainActivity, SplashScreenActivity
viewmodel/     MainViewModel — exposes categories and products
repository/    MainRepository — the only caller of the API
api/           Retrofit service interface
model/         Product and response models
adapter/       CategoryAdapter, ProductAdapter, grid spacing decoration
di/            Hilt module providing Retrofit and the service
utils/         Base URL, timeouts, the MyResult wrapper
```

## Tech stack

| Area | Libraries |
|---|---|
| Language | Kotlin |
| UI | XML layouts, Material Components, RecyclerView (grid), Lottie |
| Architecture | MVVM, repository pattern, ViewModel + lifecycle |
| Async | Kotlin Coroutines |
| Networking | Retrofit 2, Gson converter |
| DI | Hilt (Dagger) |
| Images | Glide |

## Getting started

1. Clone the repository and open it in Android Studio.
2. No API key is needed — DummyJSON is public and the base URL is already configured.
3. Run the `app` configuration on a device or emulator (minSdk 24+).

## Status

A personal project for practising REST integration and MVVM with Hilt. The catalogue flow works end to end; cart and checkout are not part of it.
