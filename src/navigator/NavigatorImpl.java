package navigator;

import route.Route;
import treemap.TreeMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NavigatorImpl implements Navigator {
    private TreeMap<String, Route> routes;

    public NavigatorImpl() {
        this.routes = new TreeMap<>();
    }

    @Override
    public void addRoute(Route route) {
        if (!routes.containsValue(route)) {
            routes.insert(route.getId(), route);
            System.out.println("Маршрут успешно добавлен.");
        }
        else System.out.println("Такой маршрут уже есть!");
    }

    @Override
    public void removeRoute(String routeId) {
        if (routes.containsValue(routes.get(routeId))) {
            routes.remove(routeId);
            System.out.println("Маршрут успешно удален.");
        }
        else System.out.println("Такого маршрута нет!");
    }

    @Override
    public boolean contains(Route route) {
        return routes.containsValue(route);
    }

    @Override
    public int size() {
        return routes.size();
    }

    @Override
    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }

    @Override
    public void chooseRoute(String routeId) {
        Route route = routes.get(routeId);
        if (route != null) {
            route.setPopularity(route.getPopularity() + 1);
        }
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        List<Route> result = new ArrayList<>();

        for (Route route : routes.values()) {
            List<String> locations = route.getLocationPoints();
            if (locations.contains(startPoint) && locations.contains(endPoint)) {
                result.add(route);
            }
        }

        result.sort(Comparator.comparing(Route::isFavorite).reversed()
                .thenComparing(r -> r.getPointSize(startPoint, endPoint)).reversed()
                .thenComparing(Route::getPopularity).reversed());

        return result;
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        List<Route> result = new ArrayList<>();

        for (Route route : routes.values()) {
            if (route.isFavorite() && !route.getLocationPoints().get(0).equals(destinationPoint) && route.getLocationPoints().contains(destinationPoint)) {
                result.add(route);
            }
        }

        result.sort(Comparator.comparing(Route::getDistance).thenComparing(Route::getPopularity).reversed());

        return result;
    }

    @Override
    public Iterable<Route> getTop3Routes() {
        List<Route> result = new ArrayList<>();
        for (Route r : routes.values()){
            result.add(r);
        }
        result.sort(Comparator.comparing(Route::getPopularity).reversed()
                .thenComparing(Route::getDistance)
                .thenComparing(route -> route.getLocationPoints().size()));

        return result.subList(0, Math.min(3, result.size()));
    }
}
