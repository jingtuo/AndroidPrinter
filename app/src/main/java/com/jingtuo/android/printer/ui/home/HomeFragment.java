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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.jingtuo.android.printer.R;
import com.jingtuo.android.printer.base.fragment.BaseFragment;
import com.jingtuo.android.printer.util.PrinterUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;

    private AppCompatTextView tvSize;

    private MediaSizeDropDownWindow mSizeDropDownWindow;

    private TextInputEditText etWidth;

    private TextInputEditText etHeight;

    private PrintJob mPrintJob;

    private MediaSizeAdapter mSizeAdapter;

    private PrintAttributes.MediaSize mCurrentMediaSize;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        tvSize = view.findViewById(R.id.tv_size);
        etWidth = view.findViewById(R.id.et_width);
        etHeight = view.findViewById(R.id.et_height);
        etWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCurrentMediaSize = null;
            }
        });
        etHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCurrentMediaSize = null;
            }
        });
        view.findViewById(R.id.btn_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etWidth.getText() == null || TextUtils.isEmpty(etWidth.getText().toString().trim())) {
                    //请输入宽度
                    Toast.makeText(v.getContext(), R.string.width_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etHeight.getText() == null || TextUtils.isEmpty(etHeight.getText().toString().trim())) {
                    //请输入高度
                    Toast.makeText(v.getContext(), R.string.height_error, Toast.LENGTH_SHORT).show();
                    return;
                }


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
                float width = Float.parseFloat(etWidth.getText().toString().trim());
                float height = Float.parseFloat(etHeight.getText().toString().trim());
                if (mCurrentMediaSize == null) {
                    String id = PrinterUtils.formatNumber(width) + "x" + PrinterUtils.formatNumber(height);
                    mCurrentMediaSize = new PrintAttributes.MediaSize(id, id, (int)PrinterUtils.convertToMils(width),
                            (int)PrinterUtils.convertToMils(height));
                }
                mCurrentMediaSize.asLandscape();
                PrintAttributes attributes = new PrintAttributes.Builder()
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .setMediaSize(mCurrentMediaSize)
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build();
                mPrintJob = printManager.print("Print Recipient", new PrintRecipientAdapter(), attributes);
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
                            PrintAttributes.MediaSize mediaSize = mSizeAdapter.getItem(position);
                            mCurrentMediaSize = mediaSize;
                            etWidth.setText(PrinterUtils.formatNumber(PrinterUtils.convertToMill(mediaSize.getWidthMils())));
                            etHeight.setText(PrinterUtils.formatNumber(PrinterUtils.convertToMill(mediaSize.getHeightMils())));
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
    }

    private PrintedPdfDocument mPdfDocument;

    class PrintRecipientAdapter extends PrintDocumentAdapter {


        @Override
        public void onStart() {
            super.onStart();
            //开始打印
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            //用户选择打印，修改打印机的配置信息，在此处进行重新布局要打印的内容
            mPdfDocument = new PrintedPdfDocument(getActivity(), newAttributes);

            if (cancellationSignal.isCanceled()) {
                //用户选择取消
                callback.onLayoutCancelled();
                return;
            }
            //每个信封打印一次，打印数量取决于收件人数量，测试阶段先设置为一个
            PrintDocumentInfo printDocumentInfo = new PrintDocumentInfo.Builder("temp.pdf")
                    .setPageCount(1)
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
            PdfDocument.Page page = mPdfDocument.startPage(0);
            drawRecipient(page);
            mPdfDocument.finishPage(page);

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
    }

    private void drawRecipient(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();

        //绘制收件人
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics());
        paint.setTextSize(textSize);
        float leftMargin = PrinterUtils.convertToPosition(20);
        float topMargin = PrinterUtils.convertToPosition(30);

        Paint.FontMetrics metrics = paint.getFontMetrics();
        float height = metrics.bottom - metrics.top;
        canvas.drawText("翟江", leftMargin, topMargin, paint);
        topMargin += height + 10;
        canvas.drawText("北京市西城区广华轩会所一层德年书画社", leftMargin, topMargin, paint);
        topMargin += height + 10;
        canvas.drawText("100055", leftMargin, topMargin, paint);
    }

}