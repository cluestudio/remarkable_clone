<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmsSkillBalance extends Struct
{
    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmsSkillBalance
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    /**
     * @return short
     */
    public function GetSkillType()
    {
        return $this->bb->getShort($this->bb_pos + 0);
    }

    /**
     * @return int
     */
    public function GetLevel()
    {
        return $this->bb->getInt($this->bb_pos + 4);
    }

    /**
     * @return float
     */
    public function GetAttackScalar()
    {
        return $this->bb->getFloat($this->bb_pos + 8);
    }

    /**
     * @return float
     */
    public function GetLifeTime()
    {
        return $this->bb->getFloat($this->bb_pos + 12);
    }

    /**
     * @return float
     */
    public function GetCoolTime()
    {
        return $this->bb->getFloat($this->bb_pos + 16);
    }

    /**
     * @return int
     */
    public function GetAbnormal()
    {
        return $this->bb->getInt($this->bb_pos + 20);
    }


    /**
     * @return int offset
     */
    public static function createRmsSkillBalance(FlatBufferBuilder $builder, $skillType, $level, $attackScalar, $lifeTime, $coolTime, $abnormal)
    {
        $builder->prep(4, 24);
        $builder->putInt($abnormal);
        $builder->putFloat($coolTime);
        $builder->putFloat($lifeTime);
        $builder->putFloat($attackScalar);
        $builder->putInt($level);
        $builder->pad(2);
        $builder->putShort($skillType);
        return $builder->offset();
    }
}
