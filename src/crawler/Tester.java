package crawler;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tester {

    private static final String[] validDateStrings = new String[]{
        "1-1-2000", //leading 0s for day and month optional
        "01-1-2000", //leading 0 for month only optional
        "1-01-2000", //leading 0 for day only optional
        "01-01-1800", //first accepted date
        "31-12-2199", //last accepted date
        "31-01-2000", //January has 31 days
        "31-03-2000", //March has 31 days
        "31-05-2000", //May has 31 days
        "31-07-2000", //July has 31 days
        "31-08-2000", //August has 31 days
        "31-10-2000", //October has 31 days
        "31-12-2000", //December has 31 days
        "30-04-2000", //April has 30 days
        "30-06-2000", //June has 30 days
        "30-09-2000", //September has 30 days
        "30-11-2000", //November has 30 days
    };
    private static final String[] invalidDateStrings = new String[]{
        "00-01-2000", //there is no 0-th day
        "01-00-2000", //there is no 0-th month
        "31-12-1799", //out of lower boundary date
        "01-01-2200", //out of high boundary date
        "32-01-2000", //January doesn't have 32 days
        "32-03-2000", //March doesn't have 32 days
        "32-05-2000", //May doesn't have 32 days
        "32-07-2000", //July doesn't have 32 days
        "32-08-2000", //August doesn't have 32 days
        "32-10-2000", //October doesn't have 32 days
        "32-12-2000", //December doesn't have 32 days
        "31-04-2000", //April doesn't have 31 days
        "31-06-2000", //June doesn't have 31 days
        "31-09-2000", //September doesn't have 31 days
        "31-11-2000", //November doesn't have 31 days
        "001-02-2000", //SimpleDateFormat valid date (day with leading 0s) even with lenient set to false
        "1-0002-2000", //SimpleDateFormat valid date (month with leading 0s) even with lenient set to false
        "01-02-0003", //SimpleDateFormat valid date (year with leading 0s) even with lenient set to false
        "01.01-2000", //. invalid separator between day and month
        "01-01.2000", //. invalid separator between month and year
        "01/01-2000", /// invalid separator between day and month
        "01-01/2000", /// invalid separator between month and year
        "01_01-2000", //_ invalid separator between day and month
        "01-01_2000", //_ invalid separator between month and year
        "01-01-2000-12345", //only whole string should be matched
        "01-13-2000", //month bigger than 13
    };

    /**
     * These constants will be used to generate the valid and invalid boundary dates for the leap years. (For no leap year, Feb. 28 valid and Feb. 29 invalid; for a leap year Feb. 29 valid and Feb. 30 invalid)   
     */
    private static final int LEAP_STEP = 4;
    private static final int YEAR_START = 1800;
    private static final int YEAR_END = 2199;

    /**
     * This date regex will find matches for valid dates between 1800 and 2199 in the format of "dd-MM-yyyy".
     * The leading 0 is optional.
     */
    private static final String DATE_REGEX = "((0?[1-9]|[12][0-9]|3[01])-(0?[13578]|1[02])-(18|19|20|21)[0-9]{2})|((0?[1-9]|[12][0-9]|30)-(0?[469]|11)-(18|19|20|21)[0-9]{2})|((0?[1-9]|1[0-9]|2[0-8])-(0?2)-(18|19|20|21)[0-9]{2})|(29-(0?2)-(((18|19|20|21)(04|08|[2468][048]|[13579][26]))|2000))";

    /**
     * This date regex is similar to the first one, but with the difference of matching only the whole string. So "01-01-2000-12345" won't pass with a match.
     * Keep in mind that String.matches tries to match only the whole string.
     */
    private static final String DATE_REGEX_ONLY_WHOLE_STRING = "^" + DATE_REGEX + "$";

    /**
     * The simple regex (without checking for 31 day months and leap years):
     */
    private static final String DATE_REGEX_SIMPLE = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((18|19|20|21)\\d\\d)";

    /**
     * This date regex is similar to the first one, but with the difference of matching only the whole string. So "01-01-2000-12345" won't pass with a match.
     */
    private static final String DATE_REGEX_SIMPLE_ONLY_WHOLE_STRING = "^" + DATE_REGEX_SIMPLE + "$";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
    static {
        SDF.setLenient(false);
    }

    private static final DateChecker dateValidatorSimple = new DateChecker(DATE_REGEX_SIMPLE);
    private static final DateChecker dateValidatorSimpleOnlyWholeString = new DateChecker(DATE_REGEX_SIMPLE_ONLY_WHOLE_STRING);


    private static void matchAssertAndPopulateTimes(
            DateTimeStatistics dateTimeStatistics,
            boolean shouldMatch, String date) {
        dateTimeStatistics.addDate(date);
        matchAndPopulateTimeToMatch(date, DATE_REGEX, shouldMatch, dateTimeStatistics.getTimesTakenWithDateRegex());
        matchAndPopulateTimeToMatch(date, DATE_REGEX_ONLY_WHOLE_STRING, shouldMatch, dateTimeStatistics.getTimesTakenWithDateRegexOnlyWholeString());
        boolean matchesSimpleDateFormat = matchWithSimpleDateFormatAndPopulateTimeToMatchAndReturnMatches(date, dateTimeStatistics.getTimesTakenWithSimpleDateFormatParse());
        matchAndPopulateTimeToMatchAndReturnMatchesAndCheck(
		                dateTimeStatistics.getTimesTakenWithDateRegexSimple(), shouldMatch,
		                date, matchesSimpleDateFormat, DATE_REGEX_SIMPLE);
        
        matchAndPopulateTimeToMatchAndReturnMatchesAndCheck(
                dateTimeStatistics.getTimesTakenWithDateRegexSimpleOnlyWholeString(), shouldMatch,
                date, matchesSimpleDateFormat, DATE_REGEX_SIMPLE_ONLY_WHOLE_STRING);

        matchAndPopulateTimeToMatch(date, dateValidatorSimple, shouldMatch, dateTimeStatistics.getTimesTakenWithdateValidatorSimple());
        matchAndPopulateTimeToMatch(date, dateValidatorSimpleOnlyWholeString, shouldMatch, dateTimeStatistics.getTimesTakenWithdateValidatorSimpleOnlyWholeString());
    }

    private static void matchAndPopulateTimeToMatchAndReturnMatchesAndCheck(
            List<Long> times,
            boolean shouldMatch, String date, boolean matchesSimpleDateFormat, String regex) {
        boolean matchesFromRegex = matchAndPopulateTimeToMatchAndReturnMatches(date, regex, times);
        assert !((matchesSimpleDateFormat && matchesFromRegex) ^ shouldMatch) : "Parsing with SimpleDateFormat and date:" + date + "\nregex:" + regex + "\nshouldMatch:" + shouldMatch;
    }

    private static void matchAndPopulateTimeToMatch(String date, String regex, boolean shouldMatch, List<Long> times) {
        boolean matches = matchAndPopulateTimeToMatchAndReturnMatches(date, regex, times);
        
        System.out.println("matchAndPopulateTimeToMatch:"+matches+" "+regex+" "+times);
        assert !(matches ^ shouldMatch) : "date:" + date + "\nregex:" + regex + "\nshouldMatch:" + shouldMatch;
    }

    private static void matchAndPopulateTimeToMatch(String date, DateChecker dateValidator, boolean shouldMatch, List<Long> times) {
        long timestampStart;
        long timestampEnd;
        boolean matches;
        timestampStart = System.nanoTime();
        matches = dateValidator.check(date);
        timestampEnd = System.nanoTime();
        times.add(timestampEnd - timestampStart);
        assert !(matches ^ shouldMatch) : "date:" + date + "\ndateValidator with regex:" + dateValidator.getRegex() + "\nshouldMatch:" + shouldMatch;
    }

    private static boolean matchAndPopulateTimeToMatchAndReturnMatches(String date, String regex, List<Long> times) {
        long timestampStart;
        long timestampEnd;
        boolean matches;
        timestampStart = System.nanoTime();
        matches = date.matches(regex);
        timestampEnd = System.nanoTime();
        times.add(timestampEnd - timestampStart);
        return matches;
    }

    private static boolean matchWithSimpleDateFormatAndPopulateTimeToMatchAndReturnMatches(String date, List<Long> times) {
        long timestampStart;
        long timestampEnd;
        boolean matches = true;
        timestampStart = System.nanoTime();
        try {
            SDF.parse(date);
        } catch (ParseException e) {
            matches = false;
        } finally {
            timestampEnd = System.nanoTime();
            times.add(timestampEnd - timestampStart);
        }
        return matches;
    }

    private static FebruaryBoundaryDates createValidAndInvalidFebruaryBoundaryDateStringsFromYear(int year) {
        FebruaryBoundaryDates februaryBoundaryDates;
        int modulo100 = year % 100;
        //http://science.howstuffworks.com/science-vs-myth/everyday-myths/question50.htm
        if ((modulo100 == 0 && year % 400 == 0) || (modulo100 != 0 && year % LEAP_STEP == 0)) {
            februaryBoundaryDates = new FebruaryBoundaryDates(
                    createFebruaryDateFromDayAndYear(29, year), 
                    createFebruaryDateFromDayAndYear(30, year)
                    );
        } else {
            februaryBoundaryDates = new FebruaryBoundaryDates(
                    createFebruaryDateFromDayAndYear(28, year), 
                    createFebruaryDateFromDayAndYear(29, year)
                    );
        }
        return februaryBoundaryDates;
    }

    private static String createFebruaryDateFromDayAndYear(int day, int year) {
        return String.format("%d-02-%d", day, year);
    }

    static class FebruaryBoundaryDates {
        private String validFebruaryBoundaryDateString;
        String invalidFebruaryBoundaryDateString;
        public FebruaryBoundaryDates(String validFebruaryBoundaryDateString,
                String invalidFebruaryBoundaryDateString) {
            super();
            this.validFebruaryBoundaryDateString = validFebruaryBoundaryDateString;
            this.invalidFebruaryBoundaryDateString = invalidFebruaryBoundaryDateString;
        }
        public String getValidFebruaryBoundaryDateString() {
            return validFebruaryBoundaryDateString;
        }
        public void setValidFebruaryBoundaryDateString(
                String validFebruaryBoundaryDateString) {
            this.validFebruaryBoundaryDateString = validFebruaryBoundaryDateString;
        }
        public String getInvalidFebruaryBoundaryDateString() {
            return invalidFebruaryBoundaryDateString;
        }
        public void setInvalidFebruaryBoundaryDateString(
                String invalidFebruaryBoundaryDateString) {
            this.invalidFebruaryBoundaryDateString = invalidFebruaryBoundaryDateString;
        }
    }

    static class DateTimeStatistics {
        private List<String> dates = new ArrayList<String>();
        private List<Long> timesTakenWithDateRegex = new ArrayList<Long>();
        private List<Long> timesTakenWithDateRegexOnlyWholeString = new ArrayList<Long>();
        private List<Long> timesTakenWithDateRegexSimple = new ArrayList<Long>();
        private List<Long> timesTakenWithDateRegexSimpleOnlyWholeString = new ArrayList<Long>();
        private List<Long> timesTakenWithSimpleDateFormatParse = new ArrayList<Long>();
        private List<Long> timesTakenWithdateValidatorSimple = new ArrayList<Long>();
        private List<Long> timesTakenWithdateValidatorSimpleOnlyWholeString = new ArrayList<Long>();
        public List<String> getDates() {
            return dates;
        }
        public List<Long> getTimesTakenWithDateRegex() {
            return timesTakenWithDateRegex;
        }
        public List<Long> getTimesTakenWithDateRegexOnlyWholeString() {
            return timesTakenWithDateRegexOnlyWholeString;
        }
        public List<Long> getTimesTakenWithDateRegexSimple() {
            return timesTakenWithDateRegexSimple;
        }
        public List<Long> getTimesTakenWithDateRegexSimpleOnlyWholeString() {
            return timesTakenWithDateRegexSimpleOnlyWholeString;
        }
        public List<Long> getTimesTakenWithSimpleDateFormatParse() {
            return timesTakenWithSimpleDateFormatParse;
        }
        public List<Long> getTimesTakenWithdateValidatorSimple() {
            return timesTakenWithdateValidatorSimple;
        }
        public List<Long> getTimesTakenWithdateValidatorSimpleOnlyWholeString() {
            return timesTakenWithdateValidatorSimpleOnlyWholeString;
        }
        public void addDate(String date) {
            dates.add(date);
        }
        public void addTimesTakenWithDateRegex(long time) {
            timesTakenWithDateRegex.add(time);
        }
        public void addTimesTakenWithDateRegexOnlyWholeString(long time) {
            timesTakenWithDateRegexOnlyWholeString.add(time);
        }
        public void addTimesTakenWithDateRegexSimple(long time) {
            timesTakenWithDateRegexSimple.add(time);
        }
        public void addTimesTakenWithDateRegexSimpleOnlyWholeString(long time) {
            timesTakenWithDateRegexSimpleOnlyWholeString.add(time);
        }
        public void addTimesTakenWithSimpleDateFormatParse(long time) {
            timesTakenWithSimpleDateFormatParse.add(time);
        }
        public void addTimesTakenWithdateValidatorSimple(long time) {
            timesTakenWithdateValidatorSimple.add(time);
        }
        public void addTimesTakenWithdateValidatorSimpleOnlyWholeString(long time) {
            timesTakenWithdateValidatorSimpleOnlyWholeString.add(time);
        }

        private void calculateAvarageTimesAndPrint() {
            long[] sumOfTimes = new long[7];
            int timesSize = timesTakenWithDateRegex.size();
            for (int i = 0; i < timesSize; i++) {
                sumOfTimes[0] += timesTakenWithDateRegex.get(i);
                sumOfTimes[1] += timesTakenWithDateRegexOnlyWholeString.get(i);
                sumOfTimes[2] += timesTakenWithDateRegexSimple.get(i);
                sumOfTimes[3] += timesTakenWithDateRegexSimpleOnlyWholeString.get(i);
                sumOfTimes[4] += timesTakenWithSimpleDateFormatParse.get(i);
                sumOfTimes[5] += timesTakenWithdateValidatorSimple.get(i);
                sumOfTimes[6] += timesTakenWithdateValidatorSimpleOnlyWholeString.get(i);
            }
            System.out.println("AVG from timesTakenWithDateRegex (in nanoseconds):" + (double) sumOfTimes[0] / timesSize);
            System.out.println("AVG from timesTakenWithDateRegexOnlyWholeString (in nanoseconds):" + (double) sumOfTimes[1] / timesSize);
            System.out.println("AVG from timesTakenWithDateRegexSimple (in nanoseconds):" + (double) sumOfTimes[2] / timesSize);
            System.out.println("AVG from timesTakenWithDateRegexSimpleOnlyWholeString (in nanoseconds):" + (double) sumOfTimes[3] / timesSize);
            System.out.println("AVG from timesTakenWithSimpleDateFormatParse (in nanoseconds):" + (double) sumOfTimes[4] / timesSize);
            System.out.println("AVG from timesTakenWithDateRegexSimple + timesTakenWithSimpleDateFormatParse (in nanoseconds):" + (double) (sumOfTimes[2] + sumOfTimes[4]) / timesSize);
            System.out.println("AVG from timesTakenWithDateRegexSimpleOnlyWholeString + timesTakenWithSimpleDateFormatParse (in nanoseconds):" + (double) (sumOfTimes[3] + sumOfTimes[4]) / timesSize);
            System.out.println("AVG from timesTakenWithdateValidatorSimple (in nanoseconds):" + (double) sumOfTimes[5] / timesSize);
            System.out.println("AVG from timesTakenWithdateValidatorSimpleOnlyWholeString (in nanoseconds):" + (double) sumOfTimes[6] / timesSize);
        }
    }

    static class DateChecker {

        private Matcher matcher;
        private Pattern pattern;

        public DateChecker(String regex) {
            pattern = Pattern.compile(regex);
        }

        /**
         * Checks if the date format is a valid.
         * Uses the regex pattern to match the date first. 
         * Than additionally checks are performed on the boundaries of the days taken the month into account (leap years are covered).
         * 
         * @param date the date that needs to be checked.
         * @return if the date is of an valid format or not.
         */
        public boolean check(final String date) {
            matcher = pattern.matcher(date);
            System.out.println("date:"+date+" "+ pattern.pattern());
            
            if (matcher.matches()) {
                matcher.reset();
                if (matcher.find()) {
                    int day = Integer.parseInt(matcher.group(1));
                    int month = Integer.parseInt(matcher.group(2));
                    int year = Integer.parseInt(matcher.group(3));

                    switch (month) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12: return day < 32;
                    case 4:
                    case 6:
                    case 9:
                    case 11: return day < 31;
                    case 2: 
                        int modulo100 = year % 100;
                        //http://science.howstuffworks.com/science-vs-myth/everyday-myths/question50.htm
                        if ((modulo100 == 0 && year % 400 == 0) || (modulo100 != 0 && year % LEAP_STEP == 0)) {
                            //its a leap year
                            return day < 30;
                        } else {
                            return day < 29;
                        }
                    default:
                        break;
                    }
                }
            }
            return false;
        }

        public String getRegex() {
            return pattern.pattern();
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        DateTimeStatistics dateTimeStatistics = new DateTimeStatistics();
        boolean shouldMatch = true;
        for (int i = 0; i < validDateStrings.length; i++) {
            String validDate = validDateStrings[i];
            System.out.println(""+validDate);
            
            matchAssertAndPopulateTimes(
                    dateTimeStatistics,
                    shouldMatch, validDate);
        }

        shouldMatch = false;
        for (int i = 0; i < invalidDateStrings.length; i++) {
            String invalidDate = invalidDateStrings[i];

            matchAssertAndPopulateTimes(dateTimeStatistics,
                    shouldMatch, invalidDate);
        }

        for (int year = YEAR_START; year < (YEAR_END + 1); year++) {
            FebruaryBoundaryDates februaryBoundaryDates = createValidAndInvalidFebruaryBoundaryDateStringsFromYear(year);
            shouldMatch = true;
            matchAssertAndPopulateTimes(dateTimeStatistics,
                    shouldMatch, februaryBoundaryDates.getValidFebruaryBoundaryDateString());
            shouldMatch = false;
            matchAssertAndPopulateTimes(dateTimeStatistics,
                    shouldMatch, februaryBoundaryDates.getInvalidFebruaryBoundaryDateString());
        }

        dateTimeStatistics.calculateAvarageTimesAndPrint();
    }
    
}