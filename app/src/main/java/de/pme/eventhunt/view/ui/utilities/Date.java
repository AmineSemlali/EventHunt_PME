package de.pme.eventhunt.view.ui.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Date {
    public String day = "";
    public String month = "";
    public String year = "";


    public void setDate(LocalDate localDate)
    {
        Integer yearInt = localDate.getYear();
        String yearString = yearInt.toString();
        this.year = yearString;

        Integer monthInt = localDate.getMonthValue();
        if(monthInt < 10) this.month = "0" + monthInt;
        else this.month = monthInt.toString();

        Integer dayInt = localDate.getDayOfMonth();
        if(dayInt < 10) this.day = "0" + dayInt;
        else this.day = dayInt.toString();


    }



    public LocalDate toLocalDate()
    {
        LocalDate localDate = LocalDate.parse(  this.year
                + "-" + this.month
                + "-" + this.day
                        );

        return localDate;
    }

    public String toLocalDateString()
    {
        return (this.year
                + "-" + this.month
                + "-" + this.day
                         );
    }

    public String formatString()
    {
        if(!this.year.isEmpty() && !this.month.isEmpty() && !this.day.isEmpty() )
        {
            String returnString = this.day + "." + this.month + "." + this.year ;
            return returnString;
        }
        else return "Error in Date::formatString";
    }

    public Date(String localDateString) throws Exception {
        if(localDateString.matches("\\d{4}-\\d{2}-\\d{2}"))
        {
            String[] parts = localDateString.split("-");
            this.year = parts[0];
            this.month = parts[1];
            this.day = parts[2];

        }
        else
        {
            throw new Exception("Date String not correct!");
        }
    }

    public boolean isComplete(){
        if(year.isEmpty() || month.isEmpty() || day.isEmpty() )
        {
            return false;
        }
        else return true;
    }

    public Date(){};
}
