package com.quitter.quitter;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.quitter.quitter.dao.DBHelper;
import com.quitter.quitter.dao.DefinediInfo;
import com.quitter.quitter.dao.TanWeiInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {
    //内存地址
    public static String root = Environment.getExternalStorageDirectory()
            .getPath();

    public static void writeExcel(Context context, List<TanWeiInfo> exportOrder,
                                  String fileName) throws Exception {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && getAvailableStorage() > 1000000) {
            ShowToast.showToast("SD卡不可用");
            return;
        }
        List<DefinediInfo> definediInfos = DBHelper.getInstance().queryDefinedInfoById("0");
        List<String> definedList = null;
        if(!CommonUtils.isListEmpty(definediInfos)){
            DefinediInfo definediInfo = definediInfos.get(0);
             definedList = definediInfo.getDefinedList();
        }else{
            ShowToast.showToast("请先自定义格式");
            return;
        }
       // String[] title = {"日期", "订单编号", "收货姓名", "收货电话", "价格", "店铺"};
        File file;
        File dir = new File(root);
        file = new File(dir, fileName + ".xls");
        if (dir.exists()) {
            dir.delete();
        }
        dir.mkdirs();

        // 创建Excel工作表
        WritableWorkbook wwb;
        OutputStream os = new FileOutputStream(file);
        wwb = Workbook.createWorkbook(os);
        // 添加第一个工作表并设置第一个Sheet的名字
        WritableSheet sheet = wwb.createSheet("订单", 0);
        Label label;
        for (int i = 0; i < definedList.size(); i++) {
            // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
            // 在Label对象的子对象中指明单元格的位置和内容
            label = new Label(i, 0, definedList.get(i), getHeader());
            // 将定义好的单元格添加到工作表中
            sheet.addCell(label);
        }

        for (int i = 0; i < exportOrder.size(); i++) {
            TanWeiInfo order = exportOrder.get(i);
            List<String> tanweiList = order.getTanweiList();
            for (int j = 0; j <tanweiList.size() ; j++) {
                Label date = new Label(j, i + 1,tanweiList.get(j));
                sheet.addCell(date);
            }


           /* Label date = new Label(0, i + 1, order.date);
            Label orderId = new Label(1, i + 1, order.orderId);
            Label name = new Label(2, i + 1, order.name);
            Label phone = new Label(3, i + 1, order.phone);
            Label amount = new Label(4, i + 1, order.amount);
            Label shop = new Label(5, i + 1, order.shop);*/

        }
        // 写入数据
        wwb.write();
        // 关闭文件
        wwb.close();
    }

    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            // format.setBorder(Border.ALL, BorderLineStyle.THIN,
            // Colour.BLACK);// 黑色边框
            // format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * 获取SD可用容量
     */
    private static long getAvailableStorage() {

        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        // Formatter.formatFileSize(context, availableSize);
        return availableSize;
    }
}
