<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmsHeroRevival extends Struct
{
    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmsHeroRevival
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    /**
     * @return int
     */
    public function GetLevel()
    {
        return $this->bb->getInt($this->bb_pos + 0);
    }

    /**
     * @return int
     */
    public function GetSec()
    {
        return $this->bb->getInt($this->bb_pos + 4);
    }


    /**
     * @return int offset
     */
    public static function createRmsHeroRevival(FlatBufferBuilder $builder, $level, $sec)
    {
        $builder->prep(4, 8);
        $builder->putInt($sec);
        $builder->putInt($level);
        return $builder->offset();
    }
}