package movieDB.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Modelova trida pro filmy.
 */
public class Movie extends Observable implements Serializable {

    private static ArrayList<Movie> movieList = new ArrayList<Movie>();

    private String name;
    private String originalName;
    private String director;
    private int rating;
    private int year;
    private String actors;
    private String zanr;
    private String about;
    private String country;

    /**
     * Metoda pro pridani noveho filmu
     *
     * @param movie
     */
    public static void addMovie(Movie movie) {
        movieList.add(movie);
    }

    /**
     * Metoda pro smazani filmu
     *
     * @param movie
     */
    public static void delMovie(Movie movie) {
        movieList.remove(movie);
    }

    /**
     * Metoda pro vyhledavani filmu.
     *
     * @param value String podle ktereho vyhledavame
     * @return
     */
    public static ObservableList<Movie> search(String value) {

        ObservableList<Movie> selectedMovies = FXCollections.observableArrayList();
        if (!(value.equals(""))) {
            for (Movie movie : Movie.getMovieData()) {
                if (movie.getName().toLowerCase().contains(value.toLowerCase())
                        || movie.getActors().toLowerCase().contains(value.toLowerCase())
                        || movie.getDirector().toLowerCase().contains(value.toLowerCase())
                        || movie.getOriginalName().toLowerCase().contains(value.toLowerCase())) {
                    selectedMovies.add(movie);
                }
            }
        }
        return selectedMovies;
    }

    /**
     * Metoda pro ulozeni dat o filmech.
     */
    public static void save() {
        try {
            FileOutputStream fos = new FileOutputStream("output.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(movieList);
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metoda pro nacteni dat o filmech ze souboru.
     */
    public static void load() {
        try {
            FileInputStream fis = new FileInputStream("output.out");
            ObjectInputStream ois = new ObjectInputStream(fis);
            movieList = (ArrayList<Movie>) ois.readObject();
            ois.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metoda pro vraceni seznamu filmovych kategorii
     * @return ObservableList<String>
     */
    public static ObservableList<String> getCategories() {
        ObservableList<String> movieCategories = FXCollections.observableArrayList();
        for (Movie movie : Movie.getMovieData()) {
            if (!(movieCategories.contains(movie.getZanr()))) {
                movieCategories.add(movie.getZanr());
            }
        }
        Collections.sort(movieCategories);
        return movieCategories;
    }

    /**
     * Metoda pro vraceni dat pro graf o poctu filmu dle zanru
     * @return ObservableList<String>
     */
    public static HashMap<String, Integer> getCountStatistics() {
        HashMap<String, Integer> data = new HashMap<String, Integer>();

        for (Movie film : movieList) {
            if (data.containsKey(film.getZanr())) {
                data.put(film.getZanr(), (data.getOrDefault(film.getZanr(), Integer.MAX_VALUE)) + 1);
            } else {
                data.put(film.getZanr(), 1);
            }
        }
        return data;
    }

    /**
     * Metoda pro vraceni dat pro graf o hodnoceni filmu dle zanru
     * @return ObservableList<String>
     */
    public static HashMap<String, Integer> getRatingStatistics() {
        HashMap<String, Integer> counts = getCountStatistics();
        HashMap<String, Integer> data = new HashMap<String, Integer>();

        for (Movie film : movieList) {
            if (data.containsKey(film.getZanr())) {
                data.put(film.getZanr(), (data.getOrDefault(film.getZanr(), Integer.MAX_VALUE)) + film.getRating());
            } else {
                data.put(film.getZanr(), film.getRating());
            }
        }

        for (String key : data.keySet()) {
            data.put(key, data.get(key) / counts.get(key));
        }

        return data;
    }

    /**
     * Metoda pro vraceni seznamu filmu, dle zvolené kategorie v treeView
     * @param value zvolena kategorie
     * @return ObservableList<Movie>
     */
    public static ObservableList<Movie> treeAction(TreeItem value) {

        ObservableList<Movie> selectedMovies = FXCollections.observableArrayList();

        if (value.getValue().equals("Všechny filmy")) {
            return FXCollections.observableArrayList(Movie.getMovieData());
        } else if (!(value.getValue().equals("Dle žánru") || value.getValue().equals("Dle země původu"))) {
            if (value.getParent().getValue().equals("Dle žánru")) {

                for (Movie movie : Movie.getMovieData()) {
                    if (value.getValue().equals(movie.getZanr())) {
                        selectedMovies.add(movie);
                    }
                }
            } else if (value.getParent().getValue().equals("Dle země původu")) {

                for (Movie movie : Movie.getMovieData()) {
                    if (value.getValue().equals(movie.getCountry())) {
                        selectedMovies.add(movie);
                    }
                }
            } else if (value.getParent().getValue().equals("Dle hodnocení")) {

                for (Movie movie : Movie.getMovieData()) {
                    if (value.getValue().equals(Integer.toString(movie.getRating()))) {
                        selectedMovies.add(movie);
                    }
                }
            }
        }
        return selectedMovies;
    }

    public Movie() {
    }

    public Movie(String name, String originalName, String director, int rating, int year, String actors, String zanr, String about, String country) {
        this.name = name;
        this.originalName = originalName;
        this.director = director;
        this.rating = rating;
        this.year = year;
        this.actors = actors;
        this.zanr = zanr;
        this.about = about;
        this.country = country;
    }

    public static ArrayList<Movie> getMovieData() {
        return movieList;
    }
    
    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getDirector() {
        return director;
    }

    public int getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    public String getActors() {
        return actors;
    }

    public String getZanr() {
        return zanr;
    }

    public String getAbout() {
        return about;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

}
