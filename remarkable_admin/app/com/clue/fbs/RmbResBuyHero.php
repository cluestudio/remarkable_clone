<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmbResBuyHero extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return RmbResBuyHero
     */
    public static function getRootAsRmbResBuyHero(ByteBuffer $bb)
    {
        $obj = new RmbResBuyHero();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmbResBuyHero
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    public function getHero()
    {
        $obj = new RmHero();
        $o = $this->__offset(4);
        return $o != 0 ? $obj->init($o + $this->bb_pos, $this->bb) : 0;
    }

    public function getAssets()
    {
        $obj = new RmAssets();
        $o = $this->__offset(6);
        return $o != 0 ? $obj->init($o + $this->bb_pos, $this->bb) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startRmbResBuyHero(FlatBufferBuilder $builder)
    {
        $builder->StartObject(2);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return RmbResBuyHero
     */
    public static function createRmbResBuyHero(FlatBufferBuilder $builder, $hero, $assets)
    {
        $builder->startObject(2);
        self::addHero($builder, $hero);
        self::addAssets($builder, $assets);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addHero(FlatBufferBuilder $builder, $hero)
    {
        $builder->addStructX(0, $hero, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addAssets(FlatBufferBuilder $builder, $assets)
    {
        $builder->addStructX(1, $assets, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endRmbResBuyHero(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}
