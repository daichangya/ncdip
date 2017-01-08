/* 
 * @(#)TimeConfigVO.java 1.0 2006-1-9
 *
 * Copyright 2006 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nc.vo.uap.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;

/**
 * 调度时间配置VO类。可根据设置的时间计算下次应调度的时间。
 * 
 * <p>
 * 第一次调用和重置使用{@link #resume()}方法，以后调用用{@link #next()}方法。目前{@link #next()}
 * 方法只适合于在将任务从任务队列转到可执行任务队列时，还直接将此循环任务加回到任务队列的情况。由{@link #currentTimeMillis()}控制。
 * 
 * <p>
 * <strong>用例描述:</strong>
 * 
 * <pre>
 * TimeConfigVO vo = new TimeConfigVO();
 * 
 * //如果以系统当前时间执行一次，则只需设置下面一行参数
 * vo.setJustInTime(true);
 * //如果想已指定时间只执行一次，则设置下面一行参数
 * vo.setJustInTime(true, System.currentTimeMillis() + 3000L); //比系统当前时间延迟3秒执行。
 * 
 * //如果是周期性执行，则设置下面的参数
 * //必须设置开始日期
 * vo.setStartDate(new UFDate(&quot;2001-01-09&quot;));
 * //如果没有设置开始时间，则默认为&quot;00:00:00&quot;
 * vo.setStartTime(new UFTime(&quot;12:00:00&quot;));
 * //如果不设置结束日期，则默认没有结束日期
 * vo.setEndDate(new UFDate(&quot;2007-01-10&quot;));
 * //如果设置了结束日期，而没有设置结束时间，则默认为&quot;23:59:59&quot;
 * vo.setEndTime(new UFTime(&quot;21:59:59&quot;));
 * 
 * //必须设置执行大于等于天的周期，如以下三种月，周和天。这三种只能设置一种。
 * //如果执行周期为月，则使用
 * vo.setPeriodUnit(PeriodUnit.MONTH);
 * vo.setPeriod(1); //每1月
 * vo.setDaysInMonth(new int[] { 1, 29 }); //月中的1日和29日
 * //如果执行周期为周，则使用
 * vo.setPeriodUnit(PeriodUnit.WEEK);
 * vo.setPeriod(2); //每2周
 * vo.setDaysInWeek(new int[] { 0, 1, 6 }); //周日，周一和周六 
 * //如果执行周期为天，则使用
 * vo.setPeriodUnit(PeriodUnit.DAY);
 * vo.setPeriod(3); //每3天
 * 
 * //以下为一天内执行的时间
 * //可以指定确定的执行时间。
 * vo.setExecTimeInDay(new UFTime(&quot;15:00:09&quot;));
 * 
 * //也可以设置一天内的招待周期，与确定的执行时间互斥
 * //必须设置一天内的开始时间。
 * vo.setStartTimeInDay(new UFTime(&quot;12:00:01&quot;));
 * //如果不设置一天内的结束时间，则默认为&quot;23:59:59&quot; 
 * vo.setEndTimeInDay(new UFTime(&quot;23:00:11&quot;));
 * 
 * //设置一天的的周期单位，包括小时，分，秒
 * vo.setPeriodUnitInDay(PeriodUnit.HOUR);
 * vo.setPeriodInDay(1); //每1小时
 * </pre>
 * 
 * 
 * <DL>
 * <DT><B>Provider:</B></DT>
 * <DD>NC-UAP</DD>
 * </DL>
 * 
 * @author chenxy
 * @since 5.0
 */
public class TimeConfigVO extends ValueObject implements TimeConst {

    private static final long serialVersionUID = -4772383247868932913L;

    /* 整个时间段的开始与结束的日期与时间。 */
    private UFDate startDate = null;
    
    //add  by xielu 记录初始状态
    private int[] startDayInMonth = null;   

    private UFTime startTime = null;

    private UFDate endDate = null;

    private UFTime endTime = null;

    /* 设置的时间单位。有效值为天，周，月。 */
    private PeriodUnit periodUnit = null;

    private int period = -1;

    /* 一周内的执行日期。 */
    private int[] daysInWeek = null;

    /* 一月内的执行日期。 */
    private int[] daysInMonth = null;

    /* 在一天内的执行时间。 */
    private UFTime execTimeInDay = null;

    /* 在一天内的时间单位与开始结束时间。与一天内的执行时间周期。 */
    private PeriodUnit periodUnitInDay = null;

    private int periodInDay = -1;

    private UFTime startTimeInDay = null;

    private UFTime endTimeInDay = null;

    /* 是否即时发生。 */
    private boolean isJustInTime = false;

    private long lastExecTime = INVALID_TIME;

    private long scheExecTime = INVALID_TIME;

    /* 是否需要结束。 */
    private boolean isTerminated = false;

    /* 是否由于一个月内的天数不够而产生的进位。如果为真，需重新设置一天内的执行时间。只为周期单位为月时使用。每用一次需重置。 */
    private boolean isDayCarriedForMonth = false;

    /* 是否被初始化（即调用过一次resume方法）。 */
    private boolean isInit = false;

    public String getEntityName() {
        return TimeConfigVO.class.getName();
    }

    public UFDate getEndDate() {
        return endDate;
    }

    public void setEndDate(UFDate endDate) {
        this.endDate = endDate;
    }

    public UFTime getEndTime() {
        return endTime;
    }

    public void setEndTime(UFTime endTime) {
        this.endTime = endTime;
    }

    public UFTime getEndTimeInDay() {
        return endTimeInDay;
    }

    public void setEndTimeInDay(UFTime endTimeInDay) {
        this.endTimeInDay = endTimeInDay;
    }

    public UFTime getExecTimeInDay() {
        return execTimeInDay;
    }

    /**
     * 
     * @param execTimeInDay
     *            在一天内的执行时间
     */
    public void setExecTimeInDay(UFTime execTimeInDay) {
        this.execTimeInDay = execTimeInDay;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriodInDay() {
        return periodInDay;
    }

    public void setPeriodInDay(int periodInDay) {
        this.periodInDay = periodInDay;
    }

    public PeriodUnit getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(PeriodUnit periodUnit) {
        this.periodUnit = periodUnit;
    }

    public PeriodUnit getPeriodUnitInDay() {
        return periodUnitInDay;
    }

    public void setPeriodUnitInDay(PeriodUnit periodUnitInDay) {
        this.periodUnitInDay = periodUnitInDay;
    }

    public UFDate getStartDate() {
        return startDate;
    }

    public void setStartDate(UFDate startDate) {
        this.startDate = startDate;
    }

    public UFTime getStartTime() {
        return startTime;
    }

    public void setStartTime(UFTime startTime) {
        this.startTime = startTime;
    }

    public UFTime getStartTimeInDay() {
        return startTimeInDay;
    }

    public void setStartTimeInDay(UFTime startTimeInDay) {
        this.startTimeInDay = startTimeInDay;
    }

    public boolean isJustInTime() {
        return isJustInTime;
    }

    /**
     * 将系统当前时间设置为执行时间。
     * 
     * @param isJustInTime
     */
    public void setJustInTime(boolean isJustInTime) {
        setJustInTime(isJustInTime, currentTimeMillis());
    }

    /**
     * 将指定时间设置为执行时间。如果指定时间比系统当前时间早，则设置为系统当前时间。
     * 
     * @param isJustInTime
     * @param time
     *            指定时间
     */
    public void setJustInTime(boolean isJustInTime, long time) {
        this.isJustInTime = isJustInTime;
        if (isJustInTime) {
            long currTime = currentTimeMillis();
            if (time < currTime)
                setScheExecTime(currTime);
            else
                setScheExecTime(time);
        }
    }

    public int[] getDaysInMonth() {
        return daysInMonth;
    }

    public void setDaysInMonth(int[] daysInMonth) {
        this.daysInMonth = daysInMonth;
    }

    public int[] getDaysInWeek() {
        return daysInWeek;
    }

    public void setDaysInWeek(int[] daysInWeek) {
        this.daysInWeek = daysInWeek;
    }

    public long getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(long lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    /**
     * 验证设置的有效性。
     */
    public void validate() throws ValidationException {
        // 如果即时发生，则直接返回。
        if (isJustInTime())
            return;
        if (null == getStartDate()) {
            throw new ValidationException("Scheduler: for time style, the start data should not be null.");
        }
        if (null == getStartTime()) {
            setStartTime(new UFTime("00:00:00"));
        }
        if (null != getEndDate() && null == getEndTime()) {
            setEndTime(new UFTime("23:59:59"));
        }
        if (null != getPeriodUnitInDay()) {
            if (getPeriodInDay() <= 0) {
                throw new ValidationException("Scheduler: period in day should be greater than 0.");
            }
            switch (getPeriodUnitInDay().intValue()) {
            case SECOND:
                if (getPeriodInDay() < 1 && getPeriodInDay() > 60) {
                    throw new ValidationException(
                            "Scheduler: period in day with unit #second# should be between 1 and 60.");
                }
                break;
            case MINUTE:
                if (getPeriodInDay() < 1 && getPeriodInDay() > 60) {
                    throw new ValidationException(
                            "Scheduler: period in day with unit #minute# should be between 1 and 60.");
                }
                break;
            case HOUR:
                if (getPeriodInDay() < 1 && getPeriodInDay() > 24) {
                    throw new ValidationException(
                            "Scheduler: period in day with unit #hour# should be between 1 and 24.");
                }
                break;
            default:
                throw new ValidationException("Scheduler: period in day should be hour, minute, or second.");
            }
            if (null == getStartTimeInDay()) {
                throw new ValidationException(
                        "Scheduler: start time in day should be set when the period in day is hour, minute, or second.");
            }
            if (null == getEndTimeInDay()) {
                setEndTimeInDay(new UFTime("23:59:59"));
            }
        }
        if (null != getPeriodUnit()) {
            if (getPeriod() <= 0) {
                throw new ValidationException("Scheduler: period should be greater than 0.");
            }
            switch (getPeriodUnit().intValue()) {
            case DAY:
                break;
            case WEEK:
                if (null == getDaysInWeek()) {
                    throw new ValidationException("Scheduler: days in week should be set with period week.");
                }
                for (int i = 0; i < getDaysInWeek().length; i++) {
                    if (getDaysInWeek()[i] < 0 || getDaysInWeek()[i] > 6) {
                        throw new ValidationException("Scheduler: day in week should between 0 and 6.");
                    }
                }
                break;
            case MONTH:
                if (null == getDaysInMonth()) {
                    throw new ValidationException("Scheduler: days in month should be set with period month.");
                }
                //add by xielu 由于标准产品月频率最后一天无法执行预警，所以新增一下逻辑代码
                if(startDayInMonth==null || (getDaysInMonth().length != startDayInMonth.length)){
                	 startDayInMonth = new int[getDaysInMonth().length];
                }
                for (int i = 0; i < getDaysInMonth().length; i++) {
                	if(getDaysInMonth()[i]==-1){ //初始状态      
                		if(lastExecTime==INVALID_TIME){
                			UFDate sdate = startDate;
                			getDaysInMonth()[i] = getLastDayOfMonth(sdate);
                		}
                		startDayInMonth[i]=-1; //copy 初始状态值
                	}else if(startDayInMonth[i]==-1){ //后期状态
                		int day = getLastDayOfNextMonth(millisToDate(scheExecTime));
                		getDaysInMonth()[i]=day;
                	}
                    if (getDaysInMonth()[i] < 1 || getDaysInMonth()[i] > 31) {
                        throw new ValidationException("Scheduler: day in month should between 1 and 31.");
                    }
                }
                //end by xielu
                break;
            default:
                throw new ValidationException("Scheduler: period should be day, week, or month.");

            }
        }
    }

    /**
     * 获得计算起始时间。
     * 
     * @return
     */
    private long currentTimeMillis() {
        // 如果运行时间无效，则返回当前时间。否则返回上次运行时间。
        if (getScheExecTime() == INVALID_TIME) {
            return System.currentTimeMillis();
        } else {
            return getScheExecTime();
        }
    }

    /* 对于定时发生的，是否已超出时间范围。 */
    private boolean isExpired() {
        if (null == getEndDate()) {
            return false;
        } else {
            // 结束日期与时间。
            // 由于GregorianCalendar返回值为0-11，而UFDate返回值为1-12，所以需要减1。
            GregorianCalendar calendar = new GregorianCalendar(getEndDate().getYear(), getEndDate().getMonth() - 1,
                    getEndDate().getDay(), getEndTime().getHour(), getEndTime().getMinute(), getEndTime().getSecond());

            if (calendar.getTimeInMillis() - currentTimeMillis() >= 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public long getScheExecTime() {
        return scheExecTime;
    }

    public String getScheExecTimeInDate() {
        return new Date(getScheExecTime()).toString();
    }

    /**
     * 如果设置了无效的时间（下次调度日期时间超过有效期或即时运行），则认为可结束。
     * 
     * @param scheExecTime
     */
    private void setScheExecTime(long scheExecTime) {    
        this.lastExecTime = this.scheExecTime;
        this.scheExecTime = scheExecTime;
    }

    /**
     * 计算在一天内的开始时间。返回{@link nc.vo.uap.scheduler.TimeConst#INVALID_TIME}表示有一天的进位。
     * 
     * @param dateTime
     *            给定起始计算时间
     * @param isInit
     *            是否第一次调用。
     * @return
     */
    private long getNextScheExecTimeInDay(UFTime dateTime, boolean isInit) {
        // 如果定义了一天确切运行的时间，则返回无效的时间。
        if (null != getExecTimeInDay()) {
            return INVALID_TIME;
        }
        int count = (int) Math.ceil((dateTime.getMillis() - getStartTimeInDay().getMillis()) * 1.0
                / getPeriodInDayWithMillis());
        // 不是第一次调用需计算下次的时间，所以加1。否则将返回当前时间点，因为当前时间点正是一个调度的时间点。
        if (!isInit)
            count++;
        long added = count * getPeriodInDayWithMillis();
        added += getStartTimeInDay().getMillis();
        if (added >= MILLIS_DAY || (null != getEndTimeInDay() && added > getEndTimeInDay().getMillis()))
            return INVALID_TIME;
        else
            return added;
    }

    /**
     * 根据给定天，找到在设置的月份中的下一个最近天。
     * 
     * @param day
     *            给定天。
     * @return
     */
    private int getNextScheExecDayInMonth(UFDate date) {
        int day = date.getDay();
        for (int i = 0; i < getDaysInMonth().length; i++) {
            if (day <= getDaysInMonth()[i] && getDaysInMonth()[i] <= date.getDaysMonth()) {
                return getDaysInMonth()[i];
            }
        }
        // 如果没有找，则返回无效时间。
        return INVALID_TIME;
    }

    /**
     * 返回以毫秒计算的周期长度。
     * 
     * @return
     */
    private long getPeriodInDayWithMillis() {
        long period = 0;
        switch (getPeriodUnitInDay().intValue()) {
        case HOUR:
            period = getPeriodInDay() * MILLIS_HOUR;
            break;
        case MINUTE:
            period = getPeriodInDay() * MILLIS_MINUTE;
            break;
        case SECOND:
            period = getPeriodInDay() * MILLIS_SECOND;
            break;
        }
        return period;
    }

    /**
     * 计算根据时间配置所设置的下次应该执行的时间，以毫秒为单位。
     * 
     * @param isInit
     *            是否第一次调用此方法。如果是，将从开始时间计算，如果否，则从上次执行时间计算。
     * @throws ValidationException
     */

    private void calcScheduledExecDateTime(boolean isInit) throws ValidationException {
        validate();
        if (isExpired()) {
            setScheExecTime(INVALID_TIME);
            return;
        }
        // 如果是即时发生的，初始化时返回当前时间，以后调用时返回无效时间。
        if (isJustInTime()) {
            if (isInit) {
                // 放在setJustInTime方法中。
                // setScheExecTime(currentTimeMillis());
            } else {
                setScheExecTime(INVALID_TIME);
            }
            return;
        }
        // 如果不是即时发生的，计算预计发生的时间。
        UFDateTime dateTime = new UFDateTime(getStartDate(), getStartTime());
        if (currentTimeMillis() > dateTime.getMillis()) {
            // 如果当前日期时间比有效期的起始日期时间大，则使用当前日期时间做为计算起始点。否则以有效期的起始日期时间做为计算起始点。
            dateTime = new UFDateTime(currentTimeMillis());
        }
        /*
         * 计算在一天内的运行时间。如果计算起始点时间比定义的一天的起始点小，则以定义的一天的起始点为一天内的运行时间。
         * 否则找到一个比计算起始点时间大的且符合定义的一天内的周期的时间点做为一天内的运行时间。
         */
        long timeInDay = INVALID_TIME;
        if (null != getExecTimeInDay()) {
            timeInDay = getExecTimeInDay().getMillis();
        } else {
            timeInDay = getStartTimeInDay().getMillis();
        }
        long settledTimeInDay = timeInDay;
        if (dateTime.getUFTime().getMillis() >= timeInDay) {
            settledTimeInDay = getNextScheExecTimeInDay(dateTime.getUFTime(), isInit);
        }

        boolean dayCarried = false;
        if (INVALID_TIME == settledTimeInDay) {
            // 有一天的进位
            dayCarried = true;
        } else {
            timeInDay = settledTimeInDay;
        }
        UFDate day = getNextScheExecDate(dayCarried);

        if (isDayCarriedForMonth) {
            // 由于月份的天数不同，会导致运行时间重新计算，取一天的最早的时间点。
            if (null != getExecTimeInDay()) {
                timeInDay = getExecTimeInDay().getMillis();
            } else {
                timeInDay = getStartTimeInDay().getMillis();
            }
            // 重置此变量。
            isDayCarriedForMonth = false;
        }
        // 将日期与时间结合起来就是下次运行的日期时间。
        setScheExecTime(day.getMillis() + timeInDay);
        
        if(isExpired()) {
        	setScheExecTime(INVALID_TIME);
        }
    }

    /**
     * 获取下一个最近的直接日期
     * 
     * @param dayCarried
     *            是否有一天的进位。
     * @return
     */
    private UFDate getNextScheExecDate(boolean dayCarried) {
        // 找到开始日期与当前日期中的大者。
        UFDate date = getStartDate();
        UFDate currDate = new UFDate(currentTimeMillis());
        if (currDate.after(date)) {
            date = currDate;
        }
        switch (getPeriodUnit().intValue()) {
        case DAY:
            if (dayCarried) {
                return new UFDate(date.getMillis() + getPeriod() * MILLIS_DAY);
            } else {
                return date;
            }
        case WEEK:
            return calcDateWithWeekPeriodUnit(date, dayCarried);
        case MONTH:
            return calcDateWithMonthPeriodUnit(date, dayCarried);
        }
        return null;
    }

    /**
     * 找到在给定日期之后的最近将来的调度日期（以周为单位）。
     * 
     * @param date
     *            给定日期
     * @param dayCarried
     *            是否有一天的进位。
     * @return
     */
    private UFDate calcDateWithWeekPeriodUnit(UFDate date, boolean dayCarried) {
        // 找到给定日期与最近将来的调度日期所差的周数。
        int diff = (date.getWeekOfYear() - getStartDate().getWeekOfYear()) % getPeriod();
        if (diff != 0) {
            diff = getPeriod() - diff;
        }

        // 计算最近将来可能的日期。
        if (dayCarried) {
            date = new UFDate(date.getMillis() + diff * MILLIS_WEEK + MILLIS_DAY);
        } else {
            date = new UFDate(date.getMillis() + diff * MILLIS_WEEK);
        }
        // 计算与最近将来所相差的天数。
        int dayInWeek = date.getWeek();
        for (int i = 0; i < getDaysInWeek().length; i++) {
            if (dayInWeek <= getDaysInWeek()[i]) {
                diff = getDaysInWeek()[i] - dayInWeek;
                break;
            } else {
                if (i == getDaysInWeek().length - 1) {
                    diff = getPeriod() * 7 + getDaysInWeek()[0] - dayInWeek;
                }
            }
        }
        return new UFDate(date.getMillis() + diff * MILLIS_DAY);
    }

    /**
     * 找到在给定日期之后的最近将来的调度日期（以月为单位）。
     * 
     * @param date
     *            给定日期
     * @param dayCarried
     *            是否有一天的进位。
     * @return
     */
    private UFDate calcDateWithMonthPeriodUnit(UFDate date, boolean dayCarried) {
        /* 计算与最近将来调度日期与相差的月数，以周期取模。 */
        int diffMonth = date.getYear() * 12 + date.getMonth() - getStartDate().getYear() * 12
                - getStartDate().getMonth();
        diffMonth %= getPeriod();
        if (diffMonth != 0) {
            diffMonth = getPeriod() - diffMonth;
        }
        int day = date.getDay();
        if (dayCarried)
            day++;

        return getNextEffectiveDate(date.getYear(), date.getMonth() + diffMonth, day);
    }

    /**
     * 由于每月的天数不一样，需找到一个有效的日期。
     * 
     * @param year
     * @param month
     *            可以大于12。
     * @param day
     * @return
     */
    private UFDate getNextEffectiveDate(int year, int month, int day) {
        if (month > 12) {
            year = year + month / 12;
            month %= 12;
            if (month == 0)
                month = 12;
        }
        UFDate tmpDate = convertToUFDate(year, month, 1);
        if (day <= tmpDate.getDaysMonth()) {
            day = getNextScheExecDayInMonth(convertToUFDate(year, month, day));
            if (INVALID_TIME != day) {
                return convertToUFDate(year, month, day);
            }
        }
        // 产生月进位。
        isDayCarriedForMonth = true;
        return getNextEffectiveDate(year, month + getPeriod(), getNextScheExecDayInMonth(convertToUFDate(year, month
                + getPeriod(), 1)));

    }

    private UFDate convertToUFDate(int year, int month, int day) {
        return new UFDate((new GregorianCalendar(year, month - 1, day)).getTimeInMillis());
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    public void setTerminated(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }

    /**
     * 第一次开始计时或在暂停后重新开始计时。
     * 
     */
    public void resume() throws ValidationException {
        // 由于即时调度时任务在设置setJustInTime时已将运行时间设置了，所以不能设置为无效时间。
        if (!isJustInTime()) {
            setScheExecTime(INVALID_TIME);
        }
        isInit = true;
        calcScheduledExecDateTime(true);
    }

    /**
     * 第二次以后调用。
     */
    public void next() throws ValidationException {
        // 防止没有调用过resume方法。
        if (!isInit) {
            resume();
        } else {
            calcScheduledExecDateTime(false);
        }
    }

    public void swapScheExecTime(TimeConfigVO tv) {
        long t = getScheExecTime();
        setScheExecTime(tv.getScheExecTime());
        tv.setScheExecTime(t);
    }
    
    /**
     * 毫秒转换成Date
     * @return
     */
    private String millisToDate(long millis){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	long now = millis;
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(now);
    	
		return sdf.format(calendar.getTime());
    }
    
    /**   
     * 获得指定日期的下一个月的最后一天   
     *    
     * @param date   
     * @return   
     */   
    public  int getLastDayOfNextMonth(String str) {   
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
        c.set(Calendar.DATE, 1);   
        c.add(Calendar.MONTH, 2);   
        c.add(Calendar.DATE, -1);   
        return c.get(Calendar.DAY_OF_MONTH);
    }  
    
    /**   
     * 获得指定日期所在当月最后一天   
     *    
     * @param date   
     * @return   
     * @throws ParseException 
     */   
    public int getLastDayOfMonth(UFDate date) {   
    	Calendar c = Calendar.getInstance();   
        c.setTime(date.toDate());   
        c.set(Calendar.DATE, 1);   
        c.add(Calendar.MONTH, 1);   
        c.add(Calendar.DATE, -1);   
        return c.get(Calendar.DAY_OF_MONTH);   
    }   
}
