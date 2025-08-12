# create_skeleton.ps1
$base = "src/main"
$pkg  = "com/example/app"

# ディレクトリ作成
$dirs = @(
  "$base/java/$pkg",
  "$base/java/$pkg/controller",
  "$base/java/$pkg/service",
  "$base/java/$pkg/service/impl",
  "$base/java/$pkg/domain",
  "$base/java/$pkg/mapper",
  "$base/resources/mapper",
  "$base/resources/templates",
  "$base/resources/static/css",
  "$base/resources/static/js"
)
$dirs | ForEach-Object { New-Item -ItemType Directory -Path $_ -Force | Out-Null }

# 空ファイル作成（必要なら後で中身を追記）
$files = @(
  "$base/java/$pkg/AppApplication.java",
  "$base/java/$pkg/controller/LoginController.java",
  "$base/java/$pkg/controller/ItemController.java",
  "$base/java/$pkg/service/ItemService.java",
  "$base/java/$pkg/service/impl/ItemServiceImpl.java",
  "$base/java/$pkg/domain/Item.java",
  "$base/java/$pkg/domain/User.java",
  "$base/java/$pkg/domain/Festival.java",
  "$base/java/$pkg/mapper/ItemMapper.java",
  "$base/java/$pkg/mapper/UserMapper.java",
  "$base/java/$pkg/mapper/FestivalMapper.java",
  "$base/resources/mapper/ItemMapper.xml",
  "$base/resources/mapper/UserMapper.xml",
  "$base/resources/mapper/FestivalMapper.xml",
  "$base/resources/templates/login.html",
  "$base/resources/templates/items_list.html",
  "$base/resources/templates/items_new.html",
  "$base/resources/templates/items_edit.html",
  "$base/resources/static/css/.gitkeep",
  "$base/resources/static/js/.gitkeep",
  "$base/resources/application.properties"
)
$files | ForEach-Object { New-Item -ItemType File -Path $_ -Force | Out-Null }

Write-Host "✅ Skeleton created."