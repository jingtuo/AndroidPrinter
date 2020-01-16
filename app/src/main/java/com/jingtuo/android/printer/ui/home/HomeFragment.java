package com.jingtuo.android.printer.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.fragment.BaseFragment;
import com.jingtuo.android.printer.data.model.MyUserInfo;
import com.jingtuo.android.printer.util.PrinterUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;

    private AppCompatTextView tvSize;

    private MediaSizeDropDownWindow mSizeDropDownWindow;

    private RadioGroup rgLp;

    private PrintJob mPrintJob;

    private MediaSizeAdapter mSizeAdapter;

    private PrintAttributes.MediaSize mCurrentMediaSize;

    private List<MyUserInfo> myUserList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        tvSize = view.findViewById(R.id.tv_size);
        rgLp = view.findViewById(R.id.rg_lp);
        view.findViewById(R.id.btn_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintManager printManager = (PrintManager) v.getContext().getSystemService(Context.PRINT_SERVICE);
                if (printManager == null) {
                    return;
                }
                if (mPrintJob != null) {
                    if (mPrintJob.isStarted() || mPrintJob.isQueued()) {
                        Toast.makeText(v.getContext(), R.string.print_job_exists, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mPrintJob.isBlocked()) {
                        mPrintJob.cancel();
                    }
                }
                if (mCurrentMediaSize == null) {
                    Toast.makeText(v.getContext(), R.string.please_select_print_size, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rgLp.getCheckedRadioButtonId() == R.id.rb_landscape) {
                    mCurrentMediaSize = mCurrentMediaSize.asLandscape();
                } else {
                    mCurrentMediaSize = mCurrentMediaSize.asPortrait();
                }
                PrintAttributes attributes = new PrintAttributes.Builder()
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .setMediaSize(mCurrentMediaSize)
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build();
                mPrintJob = printManager.print("Print Recipient",
                        new PrintRecipientAdapter(v.getContext(), myUserList), attributes);
            }
        });
        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSizeDropDownWindow == null) {
                    mSizeDropDownWindow = new MediaSizeDropDownWindow(v.getContext());
                    mSizeDropDownWindow.setAnchorView(v);
                    mSizeDropDownWindow.setWidth(v.getMeasuredWidth());
                    mSizeDropDownWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mSizeDropDownWindow.dismiss();
                            mCurrentMediaSize = mSizeAdapter.getItem(position);
                            String widthStr = PrinterUtils.formatNumber(
                                    PrinterUtils.convertToCm(mCurrentMediaSize.getWidthMils()));
                            String heightStr = PrinterUtils.formatNumber(
                                    PrinterUtils.convertToCm(mCurrentMediaSize.getHeightMils()));
                            tvSize.setText(String.format("%s (%s英寸 x %s英寸)",
                                    mCurrentMediaSize.getLabel(tvSize.getContext().getPackageManager()),
                                    widthStr, heightStr));
                        }
                    });
                }
                mSizeDropDownWindow.setAdapter(mSizeAdapter);
                mSizeDropDownWindow.show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSizeAdapter = new MediaSizeAdapter();
        homeViewModel.getSystemSizes().observe(this, new Observer<List<PrintAttributes.MediaSize>>() {
            @Override
            public void onChanged(List<PrintAttributes.MediaSize> mediaSizes) {
                mSizeAdapter.setData(mediaSizes);
            }
        });

        homeViewModel.myUserList().observe(this, new Observer<List<MyUserInfo>>() {
            @Override
            public void onChanged(List<MyUserInfo> myUserList) {
                HomeFragment.this.myUserList = myUserList;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.loadMyUserList();
    }

    private PrintedPdfDocument mPdfDocument;

    class PrintRecipientAdapter extends PrintDocumentAdapter {

        private static final int COUNT_OF_PER_PAGE = 10;

        private Context context;

        private List<MyUserInfo> myUserList;

        private int pageCount;

        private Paint mRectPaint = new Paint();

        private Paint mLinePaint = new Paint();

        private TextPaint mTextPaint = new TextPaint();

        public PrintRecipientAdapter(Context context, List<MyUserInfo> myUserList) {
            this.context = context;
            this.myUserList = myUserList;
            mRectPaint.setAntiAlias(true);
            mRectPaint.setColor(Color.BLACK);
            mRectPaint.setStyle(Paint.Style.STROKE);
            mRectPaint.setStrokeWidth(1f);

            mLinePaint.setAntiAlias(true);
            mLinePaint.setColor(Color.BLACK);
            mLinePaint.setStyle(Paint.Style.FILL);
            mLinePaint.setStrokeWidth(1f);

            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setStrokeWidth(1f);
            mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8,
                    context.getResources().getDisplayMetrics()));
        }

        @Override
        public void onStart() {
            super.onStart();
            //开始打印
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            //用户选择打印，修改打印机的配置信息，在此处进行重新布局要打印的内容
            mPdfDocument = new PrintedPdfDocument(context, newAttributes);

            if (cancellationSignal.isCanceled()) {
                //用户选择取消
                callback.onLayoutCancelled();
                return;
            }

            //每个信封打印一次，打印数量取决于收件人数量，测试阶段先设置为一个
            pageCount = calculatePageCount(newAttributes);
            PrintDocumentInfo printDocumentInfo = new PrintDocumentInfo.Builder("temp.pdf")
                    .setPageCount(pageCount)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build();
            callback.onLayoutFinished(printDocumentInfo, true);
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                return;
            }
            for (int i = 0; i < pageCount; i++) {
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    return;
                }
                PdfDocument.Page page = mPdfDocument.startPage(i);
                drawRecipient(page, i, pageCount);
                mPdfDocument.finishPage(page);

            }

            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                return;
            }

            try {
                mPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
            } catch (IOException e) {
                e.printStackTrace();
                callback.onWriteFailed(e.getMessage());
                return;
            } finally {
                mPdfDocument.close();
                mPdfDocument = null;
            }
            callback.onWriteFinished(pages);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            //打印完成
            mPrintJob = null;
        }

        private int calculatePageCount(PrintAttributes attributes) {
            PrintAttributes.MediaSize mediaSize = attributes.getMediaSize();
            int pageCount = myUserList != null ? myUserList.size() : 0;
            if (mediaSize == null) {
                return pageCount;
            }
            if (mediaSize.getWidthMils() >= PrintAttributes.MediaSize.ISO_A4.getWidthMils()
                    && mediaSize.getHeightMils() >= PrintAttributes.MediaSize.ISO_A4.getHeightMils()
                    && mediaSize.isPortrait()) {
                //比A4纸大，每页10个
                return (pageCount + COUNT_OF_PER_PAGE - 1) / COUNT_OF_PER_PAGE;
            }
            return pageCount;
        }

        private void drawRecipient(PdfDocument.Page page, int pageIndex, int pageCount) {
            int myUserCount = myUserList != null ? myUserList.size() : 0;
            Canvas canvas = page.getCanvas();
            Log.d("TEST", "canvas: " + canvas.getWidth() + "," + canvas.getHeight());
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            if (myUserCount == pageCount) {
                //每页一个
                drawOnEnvelope(canvas, myUserList.get(pageIndex));
                return;
            }

            //绘制在A4纸上
            //先绘制表格，上下左右保留2里面的距离
            float horizontalMargin = PrinterUtils.convertToPosition(20);
            float verticalMargin = PrinterUtils.convertToPosition(20);
            float left = horizontalMargin;
            float right = width - horizontalMargin;
            float top = verticalMargin;
            float bottom = height - verticalMargin;
            //绘制矩形
            canvas.drawRect(left, top, right, bottom, mRectPaint);
            //绘制一条竖线将矩形切成两个
            canvas.drawLine(width / 2f, top, width / 2f, bottom, mLinePaint);
            float averageHeight = (bottom - top) / 5f;
            //绘制4条线，将矩形切成10个
            for (int i = 0; i < 4; i++) {
                float y = top + (i + 1) * averageHeight;
                canvas.drawLine(left, y, right, y, mLinePaint);
            }

            float textHorizontalMargin = PrinterUtils.convertToPosition(5);
            float textVerticalMargin = PrinterUtils.convertToPosition(5);

            for (int i = 0; i < COUNT_OF_PER_PAGE; i++) {
                int index = pageIndex * COUNT_OF_PER_PAGE + i;
                if (index >= 0 && index < myUserCount) {
                    MyUserInfo myUserInfo = myUserList.get(i);
                    int rowIndex = i / 2;
                    float textTop = top + averageHeight * rowIndex + textVerticalMargin;
                    float textBottom = top + averageHeight * (rowIndex + 1) - textVerticalMargin;
                    float textLeft = 0f;
                    float textRight = 0f;
                    if (i % 2 == 0) {
                        //左侧
                        textLeft = left + textHorizontalMargin;
                        textRight = width / 2f - textHorizontalMargin;
                    } else {
                        //右侧
                        textLeft = width / 2f + textHorizontalMargin;
                        textRight = right - textHorizontalMargin;
                    }
                    drawAddresseeOnA4(canvas, textLeft, textTop, textRight, textBottom, myUserInfo);
                }
            }
        }

        private void drawAddresseeOnA4(Canvas canvas,
                                       float left, float top, float right, float bottom,
                                       MyUserInfo myUserInfo) {
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            StaticLayout staticLayout = StaticLayout.Builder.obtain(myUserInfo.getAddress(), 0,
                    myUserInfo.getAddress().length(), mTextPaint, (int)(right - left))
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .build();
            float height = metrics.bottom - metrics.top;
            float totalHeight = bottom - top;
            float freeSpace = totalHeight - height * 2 - staticLayout.getHeight();
            float lineInterval = freeSpace / 2;
            //绘制姓名
            canvas.drawText(myUserInfo.getFullName(), left, top - metrics.top, mTextPaint);
            top += height + lineInterval;
            //绘制地址
            canvas.translate(left, top);
            staticLayout.draw(canvas);
            canvas.translate(-left, -top);
            top += staticLayout.getHeight() + lineInterval;
            canvas.drawText(myUserInfo.getPostcode(), left, top - metrics.top, mTextPaint);
        }

        /**
         * 直接绘制在信封上
         *
         * @param canvas
         * @param myUserInfo
         */
        private void drawOnEnvelope(Canvas canvas, MyUserInfo myUserInfo) {
            //绘制收件人
            float leftMargin = PrinterUtils.convertToPosition(20);
            float topMargin = PrinterUtils.convertToPosition(30);

            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            float height = metrics.bottom - metrics.top;
            canvas.drawText(myUserInfo.getFullName(), leftMargin, topMargin - metrics.top, mTextPaint);
            topMargin += height + 10;
            canvas.drawText(myUserInfo.getAddress(), leftMargin, topMargin - metrics.top, mTextPaint);
            topMargin += height + 10;
            canvas.drawText(myUserInfo.getPostcode(), leftMargin, topMargin - metrics.top, mTextPaint);
        }
    }


}