package ru.olegsvs.excel_reader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by olegsvs on 09.04.2018.
 */

class ExcelBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ExcelItem> excelItems;

    public ExcelBookAdapter(List<ExcelItem> excelItems) {
        this.excelItems = excelItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.excel_item, parent, false);
        return new ExcelItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExcelItemHolder headerHolder = (ExcelItemHolder) holder;
        if (excelItems != null && excelItems.size() > 0) {
            ExcelItem item = excelItems.get(position);
            headerHolder.country.setText(item.getCountry());
            headerHolder.sended8.setText(item.getSended8());
            headerHolder.sended16.setText(item.getSended16());
            headerHolder.sended8caption.setText(item.getSended8caption());
            headerHolder.sended16caption.setText(item.getSended16caption());
            headerHolder.cutCaption.setText(item.getCutCaption());
            headerHolder.output.setText(item.getOutput());
        }
    }

    @Override
    public int getItemCount() {
        return excelItems.size();
    }

    class ExcelItemHolder extends RecyclerView.ViewHolder {
        TextView country;
        TextView sended8;
        TextView sended16;
        TextView sended8caption;
        TextView sended16caption;
        TextView cutCaption;
        TextView output;

        public ExcelItemHolder(View itemView) {
            super(itemView);
            country = (TextView) itemView.findViewById(R.id.country);
            sended8 = (TextView) itemView.findViewById(R.id.sended8);
            sended16 = (TextView) itemView.findViewById(R.id.sended16);
            output = (TextView) itemView.findViewById(R.id.output);
            sended8caption = (TextView) itemView.findViewById(R.id.sended8caption);
            sended16caption = (TextView) itemView.findViewById(R.id.sended16caption);
            cutCaption = (TextView) itemView.findViewById(R.id.cutCaption);
        }
    }
}
