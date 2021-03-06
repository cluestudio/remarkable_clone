<?php
// automatically generated by the FlatBuffers compiler, do not modify

namespace com\clue\fbs;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class RmmReqSync extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return RmmReqSync
     */
    public static function getRootAsRmmReqSync(ByteBuffer $bb)
    {
        $obj = new RmmReqSync();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return RmmReqSync
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    /**
     * @return sbyte
     */
    public function getPlayerNo()
    {
        $o = $this->__offset(4);
        return $o != 0 ? $this->bb->getSbyte($o + $this->bb_pos) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getOrders($j)
    {
        $o = $this->__offset(6);
        $obj = new RmmUnitOrder();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *16, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getOrdersLength()
    {
        $o = $this->__offset(6);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @returnVectorOffset
     */
    public function getHits($j)
    {
        $o = $this->__offset(8);
        $obj = new RmmUnitHit();
        return $o != 0 ? $obj->init($this->__vector($o) + $j *12, $this->bb) : null;
    }

    /**
     * @return int
     */
    public function getHitsLength()
    {
        $o = $this->__offset(8);
        return $o != 0 ? $this->__vector_len($o) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startRmmReqSync(FlatBufferBuilder $builder)
    {
        $builder->StartObject(3);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return RmmReqSync
     */
    public static function createRmmReqSync(FlatBufferBuilder $builder, $playerNo, $orders, $hits)
    {
        $builder->startObject(3);
        self::addPlayerNo($builder, $playerNo);
        self::addOrders($builder, $orders);
        self::addHits($builder, $hits);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param sbyte
     * @return void
     */
    public static function addPlayerNo(FlatBufferBuilder $builder, $playerNo)
    {
        $builder->addSbyteX(0, $playerNo, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addOrders(FlatBufferBuilder $builder, $orders)
    {
        $builder->addOffsetX(1, $orders, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createOrdersVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(16, count($data), 2);
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
    public static function startOrdersVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(16, $numElems, 2);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param VectorOffset
     * @return void
     */
    public static function addHits(FlatBufferBuilder $builder, $hits)
    {
        $builder->addOffsetX(2, $hits, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param array offset array
     * @return int vector offset
     */
    public static function createHitsVector(FlatBufferBuilder $builder, array $data)
    {
        $builder->startVector(12, count($data), 2);
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
    public static function startHitsVector(FlatBufferBuilder $builder, $numElems)
    {
        $builder->startVector(12, $numElems, 2);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endRmmReqSync(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}
