package game;

import java.lang.reflect.InvocationTargetException;

/**
 * [Thu17:00] Team1
 */

/**
 * Singleton and Factory pattern
 * for creating new strategy for every request
 */
public class StrategyFactory {
    public static StrategyFactory instance;

    public static synchronized StrategyFactory getInstance() {
        if (instance == null) {
            instance = new StrategyFactory();
        }
        return instance;
    }

//    public IRuleStrategy getNaiveFilterStrategy() {
//        IRuleStrategy strategy = null;
//        String className = System.getProperty("NaiveFilterStrategy");
//        try {
//            strategy = (IRuleStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return strategy;
//    }
//
//    public IRuleStrategy getTrumpSaveFilterStrategy() {
//        IRuleStrategy strategy = null;
//        String className = System.getProperty("TrumpSaveFilterStrategy");
//        try {
//            strategy = (IRuleStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return strategy;
//    }
//
//    public IRuleStrategy getRandomSelectStrategy() {
//        IRuleStrategy strategy = null;
//        String className = System.getProperty("RandomSelectStrategy");
//        try {
//            strategy = (IRuleStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return strategy;
//    }
//
//    public IRuleStrategy getHighestRankSelectStrategy() {
//        IRuleStrategy strategy = null;
//        String className = System.getProperty("HighestRankSelectStrategy");
//        try {
//            strategy = (IRuleStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return strategy;
//    }
//
//    public IRuleStrategy getSmartSelectStrategy() {
//        IRuleStrategy strategy = null;
//        String className = System.getProperty("SmartSelectStrategy");
//        try {
//            strategy = (IRuleStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return strategy;
//    }

    /**
     *
     * @param select String indicates which select strategy to use
     * @return the specified strategy
     */
    public ISelectStrategy getStrategy(String select) {
        ISelectStrategy strategy = null;
        String className;

        switch (select) {
            case "Random":
                className = RandomSelectStrategy.class.getName();
                break;
            case "Highest-rank":
                className = HighestRankSelectStrategy.class.getName();
                break;
            case "Smart":
                className = SmartSelectStrategy.class.getName();
                break;
            default:
                throw new IllegalStateException("Invalid select strategy: " + select);
        }

        try {
            strategy = (ISelectStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return strategy;
    }

    /**
     * Overload methods when there are 2 input parameters, now return composite strategy instead of a single strategy.
     * @param filter String indicates which filter strategy to use
     * @param select String indicates which select strategy to use
     * @return composite strategy that contains both of the strategies
     */
    public ISelectStrategy getStrategy(String filter, String select) {
        IFilterStrategy strat1 = null;
        ISelectStrategy strat2 = null;
        String className1, className2;

        switch (filter) {
            case "Naive":
                className1 = NaiveFilterStrategy.class.getName();
                break;
            case "Trump-save":
                className1 = TrumpSaveFilterStrategy.class.getName();
                break;
            default:
                throw new IllegalStateException("Invalid filter strategy: " + filter);
        }

        switch (select) {
            case "Random":
                className2 = RandomSelectStrategy.class.getName();
                break;
            case "Highest-rank":
                className2 = HighestRankSelectStrategy.class.getName();
                break;
            case "Smart":
                className2 = SmartSelectStrategy.class.getName();
                break;
            default:
                throw new IllegalStateException("Invalid select strategy: " + select);
        }

        try {
            strat1 = (IFilterStrategy) Class.forName(className1).getDeclaredConstructor().newInstance();
            strat2 = (ISelectStrategy) Class.forName(className2).getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        CompositeFilterSelectStrategy strategy = new CompositeFilterSelectStrategy(strat1, strat2);

        return strategy;
    }
}
