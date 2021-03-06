package com.zepp.www.sample.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zepp.www.sample.R;
import com.zepp.www.sample.model.Person;
import com.zepp.www.stickyheaderrecyclerview.SectionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by xubinggui on 5/23/16.
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 */

/**
 * Adapter for Person items. Sorts them by last name into sections starting with the
 * first letter of the last name.
 */
public class AddressBookAdapter extends SectionAdapter {
    Locale locale = Locale.getDefault();
    static final boolean USE_DEBUG_APPEARANCE = false;

    private class Section {
        String alpha;
        ArrayList<Person> people = new ArrayList<>();
    }

    public class ItemViewHolder extends SectionAdapter.ItemViewHolder {
        TextView personNameTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            personNameTextView = (TextView) itemView.findViewById(R.id.personNameTextView);
        }
    }

    public class HeaderViewHolder extends SectionAdapter.HeaderViewHolder {
        TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    List<Person> people;
    ArrayList<Section> sections = new ArrayList<>();

    public AddressBookAdapter() {
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
        sections.clear();

        // sort people into buckets by the first letter of last name
        char alpha = 0;
        Section currentSection = null;
        for (Person person : people) {
            if (person.name.last.charAt(0) != alpha) {
                if (currentSection != null) {
                    sections.add(currentSection);
                }

                currentSection = new Section();
                alpha = person.name.last.charAt(0);
                currentSection.alpha = String.valueOf(alpha);
            }

            if (currentSection != null) {
                currentSection.people.add(person);
            }
        }

        sections.add(currentSection);
        notifyAllSectionsDataSetChanged();
    }

    @Override public int getNumberOfSections() {
        return sections.size();
    }

    @Override public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).people.size();
    }

    @Override public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override public ItemViewHolder onCreateItemViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_addressbook_person, parent, false);
        return new ItemViewHolder(v);
    }

    @Override public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_addressbook_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressLint("SetTextI18n") @Override public void onBindItemViewHolder(
            SectionAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex) {
        Section s = sections.get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        Person person = s.people.get(itemIndex);
        ivh.personNameTextView.setText(
                capitalize(person.name.last) + ", " + capitalize(person.name.first));
    }

    @SuppressLint("SetTextI18n") @Override public void onBindHeaderViewHolder(
            SectionAdapter.HeaderViewHolder viewHolder, int sectionIndex) {
        Section s = sections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;

        if (USE_DEBUG_APPEARANCE) {
            hvh.itemView.setBackgroundColor(0x55ffffff);
            hvh.titleTextView.setText(pad(sectionIndex * 2) + s.alpha);
        } else {
            hvh.titleTextView.setText(s.alpha);
        }
    }

    private String capitalize(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase(locale) + s.substring(1);
        }

        return "";
    }

    private String pad(int spaces) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            b.append(' ');
        }
        return b.toString();
    }
}
