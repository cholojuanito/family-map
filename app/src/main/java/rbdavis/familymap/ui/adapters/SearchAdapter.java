package rbdavis.familymap.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import rbdavis.familymap.R;
import rbdavis.familymap.models.App;
import rbdavis.familymap.models.SearchResult;
import rbdavis.shared.models.data.Gender;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchResultViewHolder> {

    private List<SearchResult> results;
    private OnSearchItemClickListener clickListener;

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View v, String id, int type);
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
        clickListener = listener;
    }

    public SearchAdapter() {
    }

    public SearchAdapter(OnSearchItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_list_item, parent, false);
        return new SearchResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.bind(results.get(position));
    }

    @Override
    public int getItemCount() {
        return this.results.size();
    }

    public void setResults(List<SearchResult> results) {
        if (this.results != null) {
            this.results.clear();
        }
        this.results = results;
        notifyDataSetChanged();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {

        private SearchResult resultItem;
        private RelativeLayout layout;
        private ImageView icon;
        private TextView topLine;
        private TextView botLine;

        public SearchResultViewHolder(final View itemView) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.search_result_layout);
            icon = (ImageView) itemView.findViewById(R.id.search_result_icon);
            topLine = (TextView) itemView.findViewById(R.id.search_result_top_text);
            botLine = (TextView) itemView.findViewById(R.id.search_result_bot_text);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onSearchItemClick(itemView, resultItem.getId(), resultItem.getResultType());
                    }
                }
            });
;        }

        public void bind (SearchResult resultItem) {

            setResultItem(resultItem);

            switch (resultItem.getResultType()) {
                case SearchResult.PERSON_RESULT:
                    String personId = resultItem.getId();
                    Gender gender = App.getInstance().getPeople().get(personId).getGender();

                    if (gender == Gender.M) {
                        icon.setImageResource(R.drawable.ic_male);
                    }
                    else {
                        icon.setImageResource(R.drawable.ic_female);
                    }

                    topLine.setText(resultItem.getTopLine());
                    botLine.setText("");
                    break;

                case SearchResult.EVENT_RESULT:
                    // TODO Change icon color to the correct color
                    icon.setImageResource(R.drawable.ic_place);
                    topLine.setText(resultItem.getTopLine());
                    botLine.setText(resultItem.getBotLine());

                    break;
            }
        }

        public void setResultItem(SearchResult resultItem) {
            this.resultItem = resultItem;
        }
    }
}
