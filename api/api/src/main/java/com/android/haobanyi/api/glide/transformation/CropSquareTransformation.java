package com.android.haobanyi.api.glide.transformation;

/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
/**
 * Created by fWX228941 on 2016/5/13.
 *
 * @作者: 付敏
 * @创建日期：2016/05/13
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 * 使用说明：裁剪为一个正方形
 * bitmapTransform(new CropSquareTransformation(mContext)))
 *
 *
 */
public class CropSquareTransformation implements Transformation<Bitmap> {

  private BitmapPool mBitmapPool;
  private int mWidth;
  private int mHeight;

  public CropSquareTransformation(Context context) {
    this(Glide.get(context).getBitmapPool());
  }

  public CropSquareTransformation(BitmapPool pool) {
    this.mBitmapPool = pool;
  }

  @Override
  public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
    Bitmap source = resource.get();
    int size = Math.min(source.getWidth(), source.getHeight());

    mWidth = (source.getWidth() - size) / 2;
    mHeight = (source.getHeight() - size) / 2;

    Bitmap.Config config =
        source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;
    Bitmap bitmap = mBitmapPool.get(mWidth, mHeight, config);
    if (bitmap == null) {
      bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size);
    }

    return BitmapResource.obtain(bitmap, mBitmapPool);
  }

  @Override public String getId() {
    return "CropSquareTransformation(width=" + mWidth + ", height=" + mHeight + ")";
  }
}
