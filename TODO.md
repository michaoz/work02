# ToDo

- General
 - Enterキーで次画面遷移できなくする  5
 - 英語版でできるか試す
 - validation check 単項目、相関項目! 2
   - itemCountなどintの項目について、入力内容が数字でない場合のbeanvalidationを追加する
   - そもそもリストがnullの場合は、validationがされない → リストもnullの場合はエラーとする
 - 最後の確認画面から画面遷移できない（jsが読み込まれていない？）
 - ツールチップ
 - 画面2重送信の防止 6
 - マッピングクラスを使ってマッピングする
 - entityクラスの活用 
 
- make a new plan
  - 戻ったと際の入力状態が微妙

- create route 

- Doc
 - bindingresultの中身(errorsなど)についてまとめる	


pointGeoLayerGroupはcontrolGeoInfoに渡されたものが追加される
pointGeoLayerGroupは地図上にピンがのっているもののグループ
searchedGeoLayerMapは検索済みの全てのものが入っている

searchedGeoLayerMapからピンをたてているが、pointGeoLayerGroupから作った方がよい？



★1　jsonResultDataList（検索結果）の長さのみでcontrolGeoInfo呼び出しをループしている→検索結果しか地図上にピンを作らない
   →既存のピンも含めてcontrolGeoInfoを呼び出しする様に修正が必要。
 
- prepluggage 
 - 再訪問画面でadd recordで新しいitemを増やすと、editPrepLuggageFormでヌルポになる。 ★
 - 以前の持ち物リストを付けるようにする。履歴を見れて、現在にも適用できるようにする。

－ confirm 
 - 特にcreate routeについて、別の検索地とか追加したりソートした後の通りに表示されない（DBに正しく登録されていない？） ★
 
- first page

- button
 - スタイルの変更!

***
# Done
- 戻るボタン
- 戻るボタンで戻った画面で前の情報が表示されること




*** 
# ページにアクセスする  
 
```
http://localhost:8080/work02/travel/tripPlans/createRoute
```
