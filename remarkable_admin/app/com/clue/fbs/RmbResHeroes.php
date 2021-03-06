<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmbResHeroes extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return RmbResHeroes
     */
    public static function getRootAsRmbResHeroes(ByteBuffer $bb)
    {
        $obj = new RmbResHeroes();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmbResHeroes
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
    public function getHeroes($j)
    {
        $o = $this->__offset(4);
        $obj = new RmHero();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *24, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getHeroesLength()
    {
        $o = $this->__offset(4);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startRmbResHeroes(FlatBufferBuilder $builder)
    {
        $builder->StartObject(1);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return RmbResHeroes
     */
    public static function createRmbResHeroes(FlatBufferBuilder $builder, $heroes)
    {
        $builder->startObject(1);
        self::addHeroes($builder, $heroes);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addHeroes(FlatBufferBuilder $builder, $heroes)
    {
        $builder->addOffsetX(0, $heroes, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createHeroesVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(24, count($data), 8);
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
    public static function startHeroesVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(24, $numElems, 8);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endRmbResHeroes(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}
