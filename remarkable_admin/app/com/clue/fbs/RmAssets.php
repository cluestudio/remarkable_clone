<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmAssets extends Struct
{
    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmAssets
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
    public function GetGold()
    {
        return $this->bb->getInt($this->bb_pos + 0);
    }

    /**
     * @return int
     */
    public function GetRuby()
    {
        return $this->bb->getInt($this->bb_pos + 4);
    }


    /**
     * @return int offset
     */
    public static function createRmAssets(FlatBufferBuilder $builder, $gold, $ruby)
    {
        $builder->prep(4, 8);
        $builder->putInt($ruby);
        $builder->putInt($gold);
        return $builder->offset();
    }
}