# このリポジトリについて

このリポジトリはAndroidでのテストコードとJetpack Composeを学習するためのリポジトリです。  

# アプリの機能

- ホームタブで[PokeAPI](https://pokeapi.co/)のポケモンをリスト表示する
- ポケモンは無限スクロールで追加読み込みを行う
- 3匹までのポケモンをモンスターボールに捕獲しておき、ボールタブで詳細を確認できる
- 設定タブで利用規約を確認できる
- アプリインストール直後の起動でランダムなUUIDを生成してUIDとして永続化し、Firebase Analylytics（以下、FA）のセグメントに設定
- 捕獲/解放を行った時にFAのイベントを送信

# 設計について

このリポジトリではテストを容易にするために以下の対応を行っている。

- hiltによるDIコンテナで依存関係逆転を行い、テストの際はインターフェースに注入するものを差し替え（オーバーライド）する
- MockitoでFAのように外部へのインターフェース定義されていないサードパーティーライブラリでもテストできるようにする
- UUIDや日付などの外部環境依存するものはファクトリーをインターフェース定義し、テストの際にオーバーライドできるようにする
- 非同期についてはJetpack Composeとユニットテストの実装で相性の良いCoroutineで実装する

| 機能 | ライブラリ |
| --- | --- |
| HTTPクライアント | OkHttp |
| ローカルDB | Room |
| JSONコーダー | kotlinx-serialization |
| アプリ計測 | FA |

# テストについて

現時点でいくつかの運用パターンを実装している。

- HTTPクライアントをモックにして適切なリクエストを送信できているか
- 理想のレスポンスを返すモックサーバーを立ててレスポンスを適切に処理できるか
- 401, 500など意図的に再現することが難しいHTTP通信の状況をテストする
- テストのローカルDBに対してR/Wとテストに対するsetup/teardownを行う
- 定義したインターフェースやExtensionに対するテスト

# NOTED

- gitをクローンしただけではこのプロジェクトをビルドできません。`com.taqucinco.pokemon_sample`のパッケージに対応してFAの`google-service.json`をappフォルダ以下に配置してください。
- vi ~/.zshrcで```export JAVA_HOME=`/usr/libexec/java_home -v 17```を追加すればテストコマンド実行可能
- `brew install act`でローカルでGitHub Actionsを実行できます。```act --container-architecture linux/amd64 -W .github/workflows/unittest.yml```
- https://developer.android.com/studio/test/advanced-test-setup?hl=ja#kts
