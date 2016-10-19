<?php namespace App\Repositories;
use App\Model\Meta;
use Google\FlatBuffers\FlatbufferBuilder;
use com\clue\fbs;
use DB;

class MetaRepositoryMySql implements MetaRepository {
    public function all() {
        return DB::table('meta')
                ->select('date', 'commiter')
                ->take(10)
                ->orderBy('date', 'desc')
                ->get();
    }

    public function get($table) {
        return DB::table('meta'.$table)->get();
    }

    public function newMeta($date, $commiter, $data) {
        return DB::table('meta')->insert(
            [
                'date' => $date, 
                'commiter' => $commiter, 
                'data' => $data
            ]
        );
    }

    public function copyMeta($table, $data) {
        DB::table($table)->truncate();
        DB::table($table)->insert($data);
    }
}