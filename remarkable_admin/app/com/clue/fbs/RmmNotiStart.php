<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmmNotiStart extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return RmmNotiStart
     */
    public static function getRootAsRmmNotiStart(ByteBuffer $bb)
    {
        $obj = new RmmNotiStart();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmmNotiStart
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    /**
     * @return long
     */
    public function getStartSec()
    {
        $o = $this->__offset(4);
        return $o != 0 ? $this->bb->getLong($o + $this->bb_pos) : 0;
    }

    /**
     * @return short
     */
    public function getPlaySec()
    {
        $o = $this->__offset(6);
        return $o != 0 ? $this->bb->getShort($o + $this->bb_pos) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startRmmNotiStart(FlatBufferBuilder $builder)
    {
        $builder->StartObject(2);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return RmmNotiStart
     */
    public static function createRmmNotiStart(FlatBufferBuilder $builder, $startSec, $playSec)
    {
        $builder->startObject(2);
        self::addStartSec($builder, $startSec);
        self::addPlaySec($builder, $playSec);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param long
     * @return void
     */
    public static function addStartSec(FlatBufferBuilder $builder, $startSec)
    {
        $builder->addLongX(0, $startSec, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param short
     * @return void
     */
    public static function addPlaySec(FlatBufferBuilder $builder, $playSec)
    {
        $builder->addShortX(1, $playSec, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endRmmNotiStart(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}