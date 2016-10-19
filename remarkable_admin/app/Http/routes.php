<?php
//require '../vendor/autoload.php';
/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/
ini_set("display_errors", 1);

Route::get('/', function () {
    return view('welcome');
});

Route::get('meta/list', 'MetaController@metaList');
Route::get('meta/copy/{meta}', 'MetaController@copy');
Route::get('meta/deploy', 'MetaController@deploy');
