package app;

import navigator.Navigator;
import navigator.NavigatorImpl;
import route.Route;

import java.util.Scanner;

public class App {
    public static void run() {
        Navigator navigator = new NavigatorImpl();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addRoute(scanner, navigator);
                    break;
                case 2:
                    removeRoute(scanner, navigator);
                    break;
                case 3:
                    searchRoutes(scanner, navigator);
                    break;
                case 4:
                    getFavoriteRoutes(scanner, navigator);
                    break;
                case 5:
                    getTop3Routes(navigator);
                    break;
                case 6:
                    System.out.println("Выход из приложения.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, введите число от 1 до 6.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("––––––––– Навигатор –––––––––");
        System.out.println("1. Добавить маршрут");
        System.out.println("2. Удалить маршрут");
        System.out.println("3. Поиск маршрутов");
        System.out.println("4. Избранные маршруты");
        System.out.println("5. Топ-3 маршрута");
        System.out.println("6. Выход");
        System.out.println("–––––––––––––––––––––––––––––");
        System.out.print("Введите цифру: ");
    }

    private static void addRoute(Scanner scanner, Navigator navigator) {
        System.out.print("Введите идентификатор маршрута: ");
        String id = scanner.nextLine();
        Route route = new Route();
        route.setId(id);
        while (true) {
            System.out.print("Введите город (напишите 'next' для окончания ввода): ");
            String point = scanner.nextLine();
            if (point.equals("next")) break;
            route.addPoint(point);
        }
        System.out.print("Введите дистанцию: ");
        route.setDistance(Double.parseDouble(scanner.nextLine()));
        System.out.print("Введите уровень популярности: ");
        route.setPopularity(Integer.parseInt(scanner.nextLine()));
        System.out.print("Любимый? (true/false): ");
        route.setFavorite(Boolean.parseBoolean(scanner.nextLine()));
        navigator.addRoute(route);
    }

    private static void removeRoute(Scanner scanner, Navigator navigator) {
        System.out.print("Введите идентификатор маршрута для удаления: ");
        String routeId = scanner.nextLine();
        navigator.removeRoute(routeId);
    }

    private static void searchRoutes(Scanner scanner, Navigator navigator) {
        System.out.print("Введите начальную точку: ");
        String startPoint = scanner.nextLine();
        System.out.print("Введите конечную точку: ");
        String endPoint = scanner.nextLine();
        Iterable<Route> result = navigator.searchRoutes(startPoint, endPoint);
        System.out.println("Результаты поиска:");
        for (Route route : result) {
            System.out.println(route);
        }
    }

    private static void getFavoriteRoutes(Scanner scanner, Navigator navigator) {
        System.out.print("Введите точку назначения: ");
        String destinationPoint = scanner.nextLine();
        Iterable<Route> result = navigator.getFavoriteRoutes(destinationPoint);
        System.out.println("Избранные маршруты:");
        for (Route route : result) {
            System.out.println(route);
        }
    }

    private static void getTop3Routes(Navigator navigator) {
        Iterable<Route> result = navigator.getTop3Routes();
        System.out.println("Топ-3 маршрута:");
        for (Route route : result) {
            System.out.println(route);
        }
    }
}
