package rbdavis.familymap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.List;

import rbdavis.familymap.R;
import rbdavis.familymap.models.FamilyMemberChild;
import rbdavis.familymap.models.FamilyMemberParent;
import rbdavis.shared.models.data.Gender;

public class FamilyMemberAdapter extends ExpandableRecyclerAdapter<FamilyMemberParent, FamilyMemberChild, FamilyMemberAdapter.FamilyMemberParentViewHolder, FamilyMemberAdapter.FamilyMemberChildViewHolder> {

    private LayoutInflater layoutInflater;
    private OnChildClickListener childClickListener;

    public FamilyMemberAdapter(Context context, OnChildClickListener listener, @NonNull List<FamilyMemberParent> parentList) {
        super(parentList);
        childClickListener = listener;
        layoutInflater = LayoutInflater.from(context);
    }

    public FamilyMemberAdapter(Context context, @NonNull List<FamilyMemberParent> parentList) {
        super(parentList);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FamilyMemberParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View parentView = layoutInflater.inflate(R.layout.family_member_parent, parentViewGroup, false);
        return new FamilyMemberParentViewHolder(parentView);
    }

    @NonNull
    @Override
    public FamilyMemberChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View childView = layoutInflater.inflate(R.layout.family_member_child, childViewGroup, false);
        return new FamilyMemberChildViewHolder(childView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull FamilyMemberParentViewHolder parentViewHolder, int parentPosition, @NonNull FamilyMemberParent parent) {}

    @Override
    public void onBindChildViewHolder(@NonNull FamilyMemberChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull FamilyMemberChild child) {
        childViewHolder.bind(child);
    }

    public void setChildClickListener(FamilyMemberAdapter.OnChildClickListener clickListener) {
        this.childClickListener = clickListener;
    }

    public interface OnChildClickListener {

        void onChildClick(View v, String id);

    }


    public class FamilyMemberParentViewHolder extends ParentViewHolder {

        private ImageView dropDownArrow;

        public FamilyMemberParentViewHolder(@NonNull View itemView) {
            super(itemView);

            dropDownArrow = (ImageView) itemView.findViewById(R.id.family_members_dropdown);
        }
    }

    public class FamilyMemberChildViewHolder extends ChildViewHolder {

        private FamilyMemberChild child;
        private ImageView genderIcon;
        private TextView familyMemberName;
        private TextView familyMemberRelation;

        public FamilyMemberChildViewHolder(@NonNull final View itemView) {
            super(itemView);


            genderIcon = (ImageView) itemView.findViewById(R.id.gender_icon);
            familyMemberName = (TextView) itemView.findViewById(R.id.family_member_name);
            familyMemberRelation = (TextView) itemView.findViewById(R.id.family_member_relation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (childClickListener != null) {
                        childClickListener.onChildClick(itemView, child.getId());
                    }
                }
            });
        }

        public void bind(FamilyMemberChild memberChild) {
            child = memberChild;

            if (memberChild.getGender() == Gender.M) {
                genderIcon.setImageResource(R.drawable.ic_male);
            }
            else {
                genderIcon.setImageResource(R.drawable.ic_female);
            }
            familyMemberName.setText(memberChild.getName());
            familyMemberRelation.setText(memberChild.getRelation());
        }


    }
}
