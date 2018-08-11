package rbdavis.familymap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.List;

import rbdavis.familymap.R;
import rbdavis.familymap.models.LifeEventChild;
import rbdavis.familymap.models.LifeEventParent;

public class LifeEventAdapter extends ExpandableRecyclerAdapter<LifeEventParent, LifeEventChild, LifeEventAdapter.LifeEventParentViewHolder, LifeEventAdapter.LifeEventChildViewHolder> {

    private LayoutInflater layoutInflater;
    private OnChildClickListener childClickListener;

    public LifeEventAdapter(Context context,OnChildClickListener listener, @NonNull List<LifeEventParent> parentList) {
        super(parentList);
        childClickListener = listener;
        layoutInflater = LayoutInflater.from(context);
    }

    public LifeEventAdapter(Context context, @NonNull List<LifeEventParent> parentList) {
        super(parentList);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LifeEventParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View parentView = layoutInflater.inflate(R.layout.life_event_parent, parentViewGroup, false);
        return new LifeEventParentViewHolder(parentView);
    }

    @NonNull
    @Override
    public LifeEventChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View childView = layoutInflater.inflate(R.layout.life_event_child, childViewGroup, false);
        return new LifeEventChildViewHolder(childView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull LifeEventParentViewHolder parentViewHolder, int parentPosition, @NonNull LifeEventParent parent) {}

    @Override
    public void onBindChildViewHolder(@NonNull LifeEventChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull LifeEventChild child) {
        childViewHolder.bind(child);
    }

    public void setChildClickListener(OnChildClickListener clickListener) {
        this.childClickListener = clickListener;
    }

    public interface OnChildClickListener {

        void onChildClick(View v, String id);

    }

    public class LifeEventParentViewHolder extends ParentViewHolder {

        private ImageView dropDownArrow;

        public LifeEventParentViewHolder(@NonNull View itemView) {
            super(itemView);

            dropDownArrow = (ImageView) itemView.findViewById(R.id.life_events_dropdown);
        }
    }

    public class LifeEventChildViewHolder extends ChildViewHolder {

        private LifeEventChild child;
        private RelativeLayout layout;
        private TextView eventInfo;

        public LifeEventChildViewHolder(@NonNull final View itemView) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.event_list_item_layout);
            eventInfo = (TextView) itemView.findViewById(R.id.event_info);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (childClickListener != null) {
                        childClickListener.onChildClick(itemView, child.getId());
                    }
                }
            });
        }

        public void bind(LifeEventChild eventChild) {
            child = eventChild;

            eventInfo.setText(eventChild.getInfo());
        }

        private void startNewEventActivity(String id, View v) {

        }
    }
}
