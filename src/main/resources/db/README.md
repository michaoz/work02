# work02
work02: app to plan trips
 
  
# dockerで起動 - Dockerfileのみでの環境構築

- Dockerfileがあるディレクトリに移動する 
 
```
cd {Dockerfileがあるディレクトリ}
```

- dockerでビルドする  
 
```
docker build . -t work02-app:0.1
```
 
### ローカルDB環境

- psqlにログインする 
 
```
# postgresユーザー
psql -U psql -h localhost -p 5432 -d psql
　
# workuserユーザー
psql -U workuser -h localhost -p 5432 -d db_work
```
 

