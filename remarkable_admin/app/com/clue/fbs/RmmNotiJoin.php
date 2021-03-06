<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmmNotiJoin extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return RmmNotiJoin
     */
    public static function getRootAsRmmNotiJoin(ByteBuffer $bb)
    {
        $obj = new RmmNotiJoin();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmmNotiJoin
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    public function getPlayer0()
    {
        $obj = new RmmPlayer();
        $o = $this->__offset(4);
        return $o != 0 ? $obj->init($this->__indirect($o + $this->bb_pos), $this->bb) : 0;
    }

    public function getPlayer1()
    {
        $obj = new RmmPlayer();
        $o = $this->__offset(6);
        return $o != 0 ? $obj->init($this->__indirect($o + $this->bb_pos), $this->bb) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startRmmNotiJoin(FlatBufferBuilder $builder)
    {
        $builder->StartObject(2);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return RmmNotiJoin
     */
    public static function createRmmNotiJoin(FlatBufferBuilder $builder, $player0, $player1)
    {
        $builder->startObject(2);
        self::addPlayer0($builder, $player0);
        self::addPlayer1($builder, $player1);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addPlayer0(FlatBufferBuilder $builder, $player0)
    {
        $builder->addOffsetX(0, $player0, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addPlayer1(FlatBufferBuilder $builder, $player1)
    {
        $builder->addOffsetX(1, $player1, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endRmmNotiJoin(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}
