<?php namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Repositories\MetaRepositoryMySql;
use com\clue\fbs\RmAbnormalType;
use com\clue\fbs\RmSkillType;
use com\clue\fbs\RmUnitType;
use com\clue\fbs\RmUnitName;

use Google\FlatBuffers\FlatbufferBuilder;
use Google\FlatBuffers\ByteBuffer;
use com\clue\fbs\RmsMetaSet;
use com\clue\fbs\RmsHeroRevival;
use com\clue\fbs\RmsHeroExpUp;
use com\clue\fbs\RmsHeroExpGet;
use com\clue\fbs\RmsAbnormalBalance;
use com\clue\fbs\RmsSkillBalance;
use com\clue\fbs\RmsUnitStatic;
use com\clue\fbs\RmsUnitBalance;
use com\clue\fbs\RmsLeagueBalance;
use com\clue\fbs\RmsAnimationInfo;
use com\clue\fbs\RmsVariable;
use com\clue\fbs\RmsExp4LevelUp;

use Illuminate\Support\Facades\Redis;

class MetaController extends Controller {
    private $metaRepo;
    private $url = [];

    public function __construct(MetaRepositoryMySql $metaRepo){
        $this->metaRepo = $metaRepo;
        $this->url['HeroRevival'] = '1VSeetLJ8kmmUcFS_vvnHCOo8zh7FnCiSrHQK1SfvonI';
        $this->url['HeroExpUp'] = '132jW2odVcABjRoDOLYip5GftLiKWX4Xuwb7WXx8sjX0';
        $this->url['HeroExpGet'] = '1PQnowXZ5B8YfBhrHj4wJwL1mrpZiz0zVb78ns4QRhUY';
        $this->url['AbnormalBalance'] = '1xdxzQCWDSqnY_yCERF7MKD5XvWt8nrPayzdo_447cic';
        $this->url['SkillBalance'] = '1YWlNzhyP6MqltkfZDEfodqPGMmcAhkdxrI3q6WrullY';
        $this->url['UnitStatic'] = '1HOV_MKx781XHsa_8jRPIFpIQDY_m0G9Rh72clrxixEo';
        $this->url['UnitBalance'] = '18KPor4jDU86n_S_2ESzEksQW0JILmBJvhmUhX65XmRg';
        $this->url['LeagueBalance'] = '1nCZBHZGZ8loy9rXQCtMY5if5ZSXwqRaimgANj3Wh4Ak';
        $this->url['AnimationInfo'] = '1kedrbZIppf9zxLztTlaSw7c-coFdS6LQSq5fvhv0o2s';
        $this->url['Variable'] = '19KSL_5ldaJaeaZiF1EK0SSmnG9o5OnDOjhn-pglRjOM';
        $this->url['Exp4LevelUp'] = '1Crjl8eg0ooxfc7sM7LRrGNJwXv9R4ethMjpjbd2kU30';
    }

    public function metaList() {
        return json_encode($this->metaRepo->all());
    }

    public function deploy() {
        $builder = new FlatbufferBuilder(1);

        // Exp4LevelUp
        $row = $this->metaRepo->get('Exp4LevelUp');
        RmsMetaSet::startExp4LevelUpVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsExp4LevelUp::createRmsExp4LevelUp(
                $builder, 
                $item->level, 
                $item->exp);
        }
        $exp4LevelUp = $builder->endVector();

        // UnitStatic
        $row = $this->metaRepo->get('UnitStatic');
        RmsMetaSet::startUnitStaticVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsUnitStatic::createRmsUnitStatic($builder, 
                $item->order, 
                $item->type, 
                $item->name, 
                $item->minUserLevel, 
                $item->price, 
                $item->skill1, 
                $item->skill2, 
                $item->skill3, 
                $item->moveSpeed, 
                $item->attackCooltime, 
                $item->attackRange, 
                $item->hpRegen);
        }
        $unitStatic = $builder->endVector();

        // UnitBalance
        $row = $this->metaRepo->get('UnitBalance');
        RmsMetaSet::startUnitBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsUnitBalance::createRmsUnitBalance(
                $builder,
                $item->name, 
                $item->level, 
                $item->health,
                $item->physicalAttack, 
                $item->magicalAttack, 
                $item->physicalDefense, 
                $item->magicalDefense);
        }
        $unitBalance = $builder->endVector();

        // AbnormalBalance
        $row = $this->metaRepo->get('AbnormalBalance');
        RmsMetaSet::startAbnormalBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsAbnormalBalance::createRmsAbnormalBalance(
                $builder, 
                $item->key, 
                $item->type, 
                $item->duration, 
                $item->value);
        }
        $abnormalBalance = $builder->endVector();

        // SkillBalance
        $row = $this->metaRepo->get('SkillBalance');
        RmsMetaSet::startSkillBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsSkillBalance::createRmsSkillBalance(
                $builder, 
                $item->skillType, 
                $item->level, 
                $item->attackScalar, 
                $item->lifeTime, 
                $item->coolTime, 
                $item->abnormal);
        }
        $skillBalance = $builder->endVector();

        // HeroExpGet
        $row = $this->metaRepo->get('HeroExpGet');
        RmsMetaSet::startSkillBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsHeroExpGet::createRmsHeroExpGet($builder, $item->type, $item->exp);
        }
        $heroExpGet = $builder->endVector();

        // RmsHeroExpUp
        $row = $this->metaRepo->get('HeroExpUp');
        RmsMetaSet::startSkillBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsHeroExpUp::createRmsHeroExpUp($builder, $item->level, $item->exp);
        }
        $heroExpUp = $builder->endVector();

        // HeroRevival
        $row = $this->metaRepo->get('HeroRevival');
        RmsMetaSet::startSkillBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsHeroRevival::createRmsHeroRevival($builder, $item->level, $item->sec);
        }
        $heroRevival = $builder->endVector();

        // LeagueBalance
        $row = $this->metaRepo->get('LeagueBalance');
        RmsMetaSet::startLeagueBalanceVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsLeagueBalance::createRmsLeagueBalance(
                $builder, 
                $item->league, 
                $item->point,
                $item->gold);
        }
        $leagueBalance = $builder->endVector();

        // RmsAnimationInfo
        $row = $this->metaRepo->get('AnimationInfo');
        RmsMetaSet::startAnimationInfoVector($builder, count($row));
        foreach (array_reverse($row) as $item) {
            RmsAnimationInfo::createRmsAnimationInfo(
                $builder, 
                $item->name, 
                $item->appearLength,
                $item->stayLength,
                $item->idleLength,
                $item->runLength,
                $item->damageLength,
                $item->victoryLength,
                $item->deadLength,
                $item->attackLength,
                $item->skill1Length,
                $item->skill2Length,
                $item->skill3Length,
                $item->attackCount,
                $item->skill1Count,
                $item->skill2Count,
                $item->skill3Count);
        }
        $animationInfo = $builder->endVector();

        // variable
        $variable = $this->buildVariable($builder);

        $set = RmsMetaSet::createRmsMetaSet(
            $builder, 
            $exp4LevelUp, 
            $unitStatic, 
            $unitBalance, 
            $abnormalBalance, 
            $skillBalance, 
            $heroExpGet, 
            $heroExpUp, 
            $heroRevival,
            $leagueBalance,
            $animationInfo,
            $variable);
        $builder->finish($set);

        $data = $builder->dataBuffer()->data();
        $this->metaRepo->newMeta(time(), 'mark', $data);

        Redis::publish('remarkable-meta-channel', json_encode(['type' => 'deploy']));
        return $this->metaList();
    }

    function buildVariable($builder) {
        $playTime = 0;
        $countDownTime = 0;
        $minVersion = 0;
        $winExp = 0;
        $loseExp = 0;
        $drawExp = 0;
        $maxLeague = 0;

        $row = $this->metaRepo->get('Variable');
        foreach ($row as $item) {
            switch ($item->name) {
                case 'playTime': $playTime = $this->toInt($item->value); break;
                case 'countDownTime': $countDownTime = $this->toInt($item->value); break;
                case 'minVersion': $minVersion = $builder->createString($item->value); break;
                case 'winExp': $winExp = $this->toInt($item->value); break;
                case 'loseExp': $loseExp = $this->toInt($item->value); break;
                case 'drawExp': $drawExp = $this->toInt($item->value); break;
                case 'maxLevel': $maxLevel = $this->toInt($item->value); break;
                case 'maxLeague': $maxLeague = $this->toInt($item->value); break;
            }
        }

        $variable = RmsVariable::createRmsVariable(
            $builder, 
            $playTime, 
            $countDownTime, 
            $minVersion,
            $winExp,
            $loseExp,
            $drawExp,
            $maxLevel,
            $maxLeague);
        return $variable;
    }

    function toInt($num) {
        return intval(str_replace(",", "", $num));
    }

    function toFloat($num) {
        return floatval(str_replace(",", "", $num));
    }

    public function copy($meta) {
        $var = 0;
        return $this->copyMeta($meta, function($table, $row) {
            switch ($table) {
                case "Exp4LevelUp":
                    return [
                        'level' => $this->toInt($row[0]), 
                        'exp' => $this->toInt($row[1])
                    ];
                case "HeroRevival":
                    return [
                        'level' => $this->toInt($row[0]), 
                        'sec' => $this->toInt($row[1])
                    ];
                case "HeroExpUp":
                    return [
                        'level' => $this->toInt($row[0]), 
                        'exp' => $this->toInt($row[1])
                    ];
                case "HeroExpGet":
                    return [
                        'type' => constant('com\clue\fbs\RmUnitType::'.$row[0]), 
                        'exp' => $this->toInt($row[1])
                    ];
                case "AbnormalBalance":
                    return [
                        'key' => $this->toInt($row[0]), 
                        'type' => constant('com\clue\fbs\RmAbnormalType::'.$row[1]),
                        'duration' => $this->toFloat($row[2]), 
                        'value' => $this->toInt($row[3])
                    ];
                case "SkillBalance":
                    return [
                        'skillType' => constant('com\clue\fbs\RmSkillType::'.$row[0]),
                        'level' => $this->toInt($row[1]), 
                        'attackScalar' => $this->toFloat($row[2]), 
                        'lifeTime' => $this->toFloat($row[3]), 
                        'coolTime' => $this->toFloat($row[4]), 
                        'abnormal' => $this->toInt($row[5])
                    ];
                case "UnitStatic":
                    return [
                        'order' => $this->toInt($row[0]), 
                        'type' => constant('com\clue\fbs\RmUnitType::'.$row[1]),
                        'name' => constant('com\clue\fbs\RmUnitName::'.$row[2]),
                        'minUserLevel' => $this->toInt($row[3]), 
                        'price' => $this->toInt($row[4]), 
                        'skill1' => constant('com\clue\fbs\RmSkillType::'.$row[5]),
                        'skill2' => constant('com\clue\fbs\RmSkillType::'.$row[6]),
                        'skill3' => constant('com\clue\fbs\RmSkillType::'.$row[7]),
                        'moveSpeed' => $this->toFloat($row[8]), 
                        'attackCooltime' => $this->toFloat($row[9]), 
                        'attackRange' => $this->toFloat($row[10]),
                        'hpRegen' => $this->toFloat($row[11])
                    ];
                case "UnitBalance":
                    return [
                        'name' => constant('com\clue\fbs\RmUnitName::'.$row[0]),
                        'level' => $this->toInt($row[1]), 
                        'health' => $this->toFloat($row[2]), 
                        'physicalAttack' => $this->toFloat($row[3]), 
                        'magicalAttack' => $this->toFloat($row[4]), 
                        'physicalDefense' => $this->toFloat($row[5]), 
                        'magicalDefense' => $this->toFloat($row[6])
                    ];                    
                case "AnimationInfo":
                    return [
                        'name' => $this->toInt($row[0]),
                        'appearLength' => $this->toInt($row[1]), 
                        'stayLength' => $this->toFloat($row[2]), 
                        'idleLength' => $this->toFloat($row[3]), 
                        'runLength' => $this->toFloat($row[4]), 
                        'damageLength' => $this->toInt($row[5]), 
                        'victoryLength' => $this->toFloat($row[6]), 
                        'deadLength' => $this->toFloat($row[7]), 
                        'attackLength' => $this->toInt($row[8]), 
                        'skill1Length' => $this->toFloat($row[9]), 
                        'skill2Length' => $this->toFloat($row[10]), 
                        'skill3Length' => $this->toFloat($row[11]), 
                        'attackCount' => $this->toInt($row[12]), 
                        'skill1Count' => $this->toInt($row[13]),
                        'skill2Count' => $this->toInt($row[14]),
                        'skill3Count' => $this->toInt($row[15])
                    ];                    
                case "LeagueBalance":
                    return [
                        'league' => $this->toInt($row[0]), 
                        'point' => $this->toInt($row[1]),
                        'gold' => $this->toInt($row[2])
                    ];                    
                case "Variable":
                    return [
                        'name' => trim($row[0]), 
                        'value' => trim($row[1])
                    ];                    
            }
            return "";
        });
    }

    function copyMeta($table, $callback) {
        $client = new \GuzzleHttp\Client();
        $res = $client->request('GET', $this->GetGooleDocsCsv($this->url[$table]));
        $csv = $this->parseCsv($res->getBody());

        $array = [];        
        foreach ($csv as $row) {
            $array[] = $callback($table, $row);
        }

        $this->metaRepo->copyMeta('meta'.$table, $array);
        return json_encode($array);
    }

    function parseCsv($text) {
        $array = explode("\n", $text);
        $result = [];
        for ($i = 1; $i < count($array); $i++) {
            $result[] = explode(",", $array[$i]);
        }
        return $result;
    }

    function GetGooleDocsCsv($key) {
        return 'https://docs.google.com/spreadsheets/d/'.$key.'/pub?gid=0&single=true&output=csv';
    }
}
