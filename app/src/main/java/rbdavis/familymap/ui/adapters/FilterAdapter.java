package rbdavis.familymap.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private List<String> filters;

    public FilterAdapter(List<String> filters) {
        this.filters = filters;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.filter_list_item, parent, false);

        return new FilterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        holder.bind(filters.get(position));
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {

        private TextView header;
        private TextView subHeader;
        private Switch filterSwitch;
        private String eventType;

        public FilterViewHolder(View itemView) {
            super(itemView);

            header = (TextView) itemView.findViewById(R.id.filter_header);
            subHeader = (TextView) itemView.findViewById(R.id.filter_subheader);
            filterSwitch = (Switch) itemView.findViewById(R.id.filter_switch);

            filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateFilter(isChecked);
                }
            });
        }

        public void bind(String type) {
            eventType = type;

            header.setText(type);
            filterSwitch.setChecked(App.getInstance().getFilters().getFilterOptions().get(type));

            updateText();
        }

        public void updateFilter(boolean isChecked) {
            App.getInstance().getFilters().getFilterOptions().put(eventType, isChecked);
            updateText();
        }

        private void updateText() {
            if (filterSwitch.isChecked()) {
                String subHead = "Showing " + eventType + " events";
                subHeader.setText(subHead);
            }
            else {
                String subHead = "Not showing " + eventType + " events";
                subHeader.setText(subHead);
            }
        }
    }
}
