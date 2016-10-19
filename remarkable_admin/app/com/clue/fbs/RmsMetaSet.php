<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmsMetaSet extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return RmsMetaSet
     */
    public static function getRootAsRmsMetaSet(ByteBuffer $bb)
    {
        $obj = new RmsMetaSet();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmsMetaSet
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    /**
     * @returnVectorOffset
     */
    public function getExp4LevelUp($j)
    {
        $o = $this->__offset(4);
        $obj = new RmsExp4LevelUp();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *8, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getExp4LevelUpLength()
    {
        $o = $this->__offset(4);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getUnitStatic($j)
    {
        $o = $this->__offset(6);
        $obj = new RmsUnitStatic();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *36, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getUnitStaticLength()
    {
        $o = $this->__offset(6);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getUnitBalance($j)
    {
        $o = $this->__offset(8);
        $obj = new RmsUnitBalance();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *28, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getUnitBalanceLength()
    {
        $o = $this->__offset(8);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getAbnormalBalance($j)
    {
        $o = $this->__offset(10);
        $obj = new RmsAbnormalBalance();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *16, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getAbnormalBalanceLength()
    {
        $o = $this->__offset(10);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getSkillBalance($j)
    {
        $o = $this->__offset(12);
        $obj = new RmsSkillBalance();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *24, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getSkillBalanceLength()
    {
        $o = $this->__offset(12);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getHeroExpGet($j)
    {
        $o = $this->__offset(14);
        $obj = new RmsHeroExpGet();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *8, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getHeroExpGetLength()
    {
        $o = $this->__offset(14);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getHeroExpUp($j)
    {
        $o = $this->__offset(16);
        $obj = new RmsHeroExpUp();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *8, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getHeroExpUpLength()
    {
        $o = $this->__offset(16);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getHeroRevival($j)
    {
        $o = $this->__offset(18);
        $obj = new RmsHeroRevival();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *8, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getHeroRevivalLength()
    {
        $o = $this->__offset(18);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getLeagueBalance($j)
    {
        $o = $this->__offset(20);
        $obj = new RmsLeagueBalance();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *12, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getLeagueBalanceLength()
    {
        $o = $this->__offset(20);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getAnimationInfo($j)
    {
        $o = $this->__offset(22);
        $obj = new RmsAnimationInfo();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *24, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getAnimationInfoLength()
    {
        $o = $this->__offset(22);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    public function getVariable()
    {
        $obj = new RmsVariable();
        $o = $this->__offset(24);
        return $o != 0 ? $obj->init($this->__indirect($o + $this->bb_pos), $this->bb) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startRmsMetaSet(FlatBufferBuilder $builder)
    {
        $builder->StartObject(11);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return RmsMetaSet
     */
    public static function createRmsMetaSet(FlatBufferBuilder $builder, $exp4LevelUp, $unitStatic, $unitBalance, $abnormalBalance, $skillBalance, $heroExpGet, $heroExpUp, $heroRevival, $leagueBalance, $animationInfo, $variable)
    {
        $builder->startObject(11);
        self::addExp4LevelUp($builder, $exp4LevelUp);
        self::addUnitStatic($builder, $unitStatic);
        self::addUnitBalance($builder, $unitBalance);
        self::addAbnormalBalance($builder, $abnormalBalance);
        self::addSkillBalance($builder, $skillBalance);
        self::addHeroExpGet($builder, $heroExpGet);
        self::addHeroExpUp($builder, $heroExpUp);
        self::addHeroRevival($builder, $heroRevival);
        self::addLeagueBalance($builder, $leagueBalance);
        self::addAnimationInfo($builder, $animationInfo);
        self::addVariable($builder, $variable);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addExp4LevelUp(FlatBufferBuilder $builder, $exp4LevelUp)
    {
        $builder->addOffsetX(0, $exp4LevelUp, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createExp4LevelUpVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(8, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startExp4LevelUpVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(8, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addUnitStatic(FlatBufferBuilder $builder, $unitStatic)
    {
        $builder->addOffsetX(1, $unitStatic, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createUnitStaticVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(36, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startUnitStaticVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(36, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addUnitBalance(FlatBufferBuilder $builder, $unitBalance)
    {
        $builder->addOffsetX(2, $unitBalance, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createUnitBalanceVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(28, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startUnitBalanceVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(28, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addAbnormalBalance(FlatBufferBuilder $builder, $abnormalBalance)
    {
        $builder->addOffsetX(3, $abnormalBalance, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createAbnormalBalanceVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(16, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startAbnormalBalanceVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(16, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addSkillBalance(FlatBufferBuilder $builder, $skillBalance)
    {
        $builder->addOffsetX(4, $skillBalance, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createSkillBalanceVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(24, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startSkillBalanceVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(24, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addHeroExpGet(FlatBufferBuilder $builder, $heroExpGet)
    {
        $builder->addOffsetX(5, $heroExpGet, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createHeroExpGetVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(8, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startHeroExpGetVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(8, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addHeroExpUp(FlatBufferBuilder $builder, $heroExpUp)
    {
        $builder->addOffsetX(6, $heroExpUp, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createHeroExpUpVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(8, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startHeroExpUpVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(8, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addHeroRevival(FlatBufferBuilder $builder, $heroRevival)
    {
        $builder->addOffsetX(7, $heroRevival, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createHeroRevivalVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(8, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startHeroRevivalVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(8, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addLeagueBalance(FlatBufferBuilder $builder, $leagueBalance)
    {
        $builder->addOffsetX(8, $leagueBalance, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createLeagueBalanceVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(12, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startLeagueBalanceVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(12, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addAnimationInfo(FlatBufferBuilder $builder, $animationInfo)
    {
        $builder->addOffsetX(9, $animationInfo, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createAnimationInfoVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(24, count($data), 4);
        for ($i = count($data) - 1; $i >= 0; $i--) {
            $builder->addOffset($data[$i]);
        }
        return $builder->endVector();
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int $numElems
     * @return void
     */
    public static function startAnimationInfoVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(24, $numElems, 4);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addVariable(FlatBufferBuilder $builder, $variable)
    {
        $builder->addOffsetX(10, $variable, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endRmsMetaSet(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}