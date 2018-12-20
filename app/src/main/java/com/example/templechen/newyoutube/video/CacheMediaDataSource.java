package com.example.templechen.newyoutube.video;

import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

import java.io.IOException;

public class CacheMediaDataSource implements IMediaDataSource {

    /**
     *
     * @param position 视频流读取进度
     * @param buffer 要把读取到的数据存到这个数组
     * @param offset 数据开始写入的坐标
     * @param size 本次一共读取数据的大小
     * @throws IOException
     */
    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        return 0;
    }

    @Override
    public long getSize() throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
