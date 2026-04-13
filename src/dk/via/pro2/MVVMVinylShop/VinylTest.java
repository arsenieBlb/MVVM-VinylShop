package dk.via.pro2.MVVMVinylShop;

import dk.via.pro2.MVVMVinylShop.model.Person;
import dk.via.pro2.MVVMVinylShop.model.Vinyl;
import dk.via.pro2.MVVMVinylShop.model.VinylModelManager;
import dk.via.pro2.MVVMVinylShop.view.ViewHandler;
import dk.via.pro2.MVVMVinylShop.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class VinylTest extends Application
{
  private static VinylModelManager model;

  public static void main(String[] args)
  {
    model = new VinylModelManager();

    // Vinyls
    model.add(new Vinyl("Dark Side of the Moon",   "Pink Floyd",          1973));
    model.add(new Vinyl("Abbey Road",               "The Beatles",         1969));
    model.add(new Vinyl("Rumours",                  "Fleetwood Mac",       1977));
    model.add(new Vinyl("Led Zeppelin IV",          "Led Zeppelin",        1971));
    model.add(new Vinyl("Thriller",                 "Michael Jackson",     1982));
    model.add(new Vinyl("Back in Black",            "AC/DC",               1980));
    model.add(new Vinyl("Nevermind",                "Nirvana",             1991));
    model.add(new Vinyl("Purple Rain",              "Prince",              1984));
    model.add(new Vinyl("Born to Run",              "Bruce Springsteen",   1975));
    model.add(new Vinyl("Exile on Main St.",        "The Rolling Stones",  1972));

    // Persons
    ArrayList<Person> persons = new ArrayList<>();
    persons.add(new Person("Alice",   "Smith"));
    persons.add(new Person("Bob",     "Jones"));
    persons.add(new Person("Charlie", "Hansen"));
    persons.add(new Person("Diana",   "Nielsen"));
    persons.add(new Person("Erik",    "Jensen"));


    model.addListener(evt ->
    {
      System.out.println("\n[MODEL UPDATE]");
      ArrayList<Vinyl> updated = model.getVinyls();
      for (int i = 0; i < updated.size(); i++)
      {
        Vinyl v = updated.get(i);
        System.out.println(
            "  - " + v.getTitle()
            + " | " + v.getStateLabel()
            + " | borrowed by: " + (v.getBorrowedBy() != null ? v.getBorrowedBy().getFirstName() : "nobody")
            + " | reserved by: " + (v.getReservedBy() != null ? v.getReservedBy().getFirstName() : "nobody"));
      }
    });


    for (int i = 0; i < persons.size(); i++)
    {
      Person person = persons.get(i);

      Thread t = new Thread(() ->
      {
        Random random = new Random();

        while (true)
        {
          ArrayList<Vinyl> vinyls = model.getVinyls();

          Vinyl vinyl = vinyls.get(random.nextInt(vinyls.size()));

          Person borrower  = vinyl.getBorrowedBy();
          Person reserver  = vinyl.getReservedBy();

          if (borrower != null && borrower.getId() == person.getId())
          {
            System.out.println("[" + person.getFirstName() + "] returning: " + vinyl.getTitle());
            try
            {
              model.returnVinyl(vinyl, person);
              System.out.println("[" + person.getFirstName() + "] returned: " + vinyl.getTitle());
            }
            catch (Exception e)
            {
              System.out.println("[" + person.getFirstName() + "] return failed: " + e.getMessage());
            }
          }

          else if (reserver != null && reserver.getId() == person.getId())
          {
            System.out.println("[" + person.getFirstName() + "] borrowing own reservation: " + vinyl.getTitle());
            try
            {
              model.borrow(vinyl, person);
              System.out.println("[" + person.getFirstName() + "] borrowed: " + vinyl.getTitle());
            }
            catch (Exception e)
            {
              System.out.println("[" + person.getFirstName() + "] borrow failed: " + e.getMessage());
            }
          }

          else
          {
            if (random.nextInt(2) == 0)
            {
              System.out.println("[" + person.getFirstName() + "] trying to reserve: " + vinyl.getTitle());
              try
              {
                model.reserve(vinyl, person);
                System.out.println("[" + person.getFirstName() + "] reserved: " + vinyl.getTitle());
              }
              catch (Exception e)
              {
                System.out.println("[" + person.getFirstName() + "] reserve failed: " + e.getMessage());
              }
            }
            else
            {
              System.out.println("[" + person.getFirstName() + "] trying to borrow: " + vinyl.getTitle());
              try
              {
                model.borrow(vinyl, person);
                System.out.println("[" + person.getFirstName() + "] borrowed: " + vinyl.getTitle());
              }
              catch (Exception e)
              {
                System.out.println("[" + person.getFirstName() + "] borrow failed: " + e.getMessage());
              }
            }
          }


          try
          {
            Thread.sleep(100000);
          }
          catch (InterruptedException e)
          {
            Thread.currentThread().interrupt();
            return;
          }
        }
      });

      t.start();
    }

    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);
  }

}