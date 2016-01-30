package bristol.zepp.com.databindingdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by xubinggui on 1/30/16.
 */
public class User extends BaseObservable{

    public final boolean isFriend;
    public final boolean isAdult;

    public User(String firstName, String lastName, boolean isFirend, boolean isAdult) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isFriend = isFirend;
        this.isAdult = isAdult;
    }

    private String firstName;
    private String lastName;

    @Bindable public String getFirstName() {
        return this.firstName;
    }

    @Bindable public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(bristol.zepp.com.databindingdemo.BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(bristol.zepp.com.databindingdemo.BR.lastName);
    }
}
