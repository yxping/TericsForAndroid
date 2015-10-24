package com.scnu.yxp.tetris.component;

import com.scnu.yxp.tetris.base.Block;
import com.scnu.yxp.tetris.base.Component;
import com.scnu.yxp.tetris.logic.Constants;

/**
 * Created by yxp on 2015/10/20.
 */
public class TComponent extends Component {

    public TComponent(int resId, int mode) {
        super(resId, mode);
        int mid = Constants.WIDTH_NUM / 2;
        blocks[0] = new Block(resId, mid, 0);// 中心点
        blocks[1] = new Block(resId, mid - 1, 0);
        blocks[2] = new Block(resId, mid + 1, 0);
        blocks[3] = new Block(resId, mid, -1);
        while(curMode != mode){
            switchMode();
        }
    }
}
