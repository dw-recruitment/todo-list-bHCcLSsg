# ducks

A small TODO web app; Democracy Works programming assignment.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Notes and Observations

### Step 1

I used `lein new compojure-app ducks` to stand up the project.  This
lays down an initial structure that most closely matches what I image the
final Step 10 will need.

I started dithering about what to call the project.  When I realized I
was dithering, I decided to look out the window and name it after the
first thing I saw.  There were a bunch of mallards looking very happy
in the rain so I called it "ducks".

### Step 2

The template set up ring out of the box but more dithering on the
gifs.  Gifs are fun.  I ended up going with the building of the
pyramids.

I'm not really thrilled with `hiccup-elements` but I'm not sure what
the convention is.  I'm also not thrilled with the hiccup in the
handler.  Isn't that what the view directory is for?

### Step 3

More hiccup in the handler, this time the about endpoint.  :(

### Step 4

#### migrations

This isn't required but all apps with a db are going to need a
migration script.  I would rather use something off the shelf than
roll my own.  clojure-toolbox lists six different libraries for doing
migrations.  Drift looked liked the closest to what I wanted then
turned out to not be.  Oh well.  What I wanted was straight up sql
files somewhere; instead I've got a .clj file with sql strings in it.
I think it would be easier to set up and trouble shoot if it weren't
tied so closely to clojure.

I'm getting the migration "working" but definitely cutting corners on
testing.  Need to factor that into the imaginary "iteration 2"
estimate.

#### db

For the database, going with sqlite.  I've used it before, but
straight up on it's own - not a clojure project.  H2 probably would
have been faster to get going but I kind of overdosed on H2 at the
last place and it can be a little squirrely.  Postgresql is fine but
sqlite is lighter; going with lighter.

I like the yesql sales pitch.  Sql vs. s-expression domain language
simplifies debugging issues down the road.

#### tests

Hmm.  I have gaps in my test coverage.  I would really like unit tests
for the logic and integration tests actually putting stuff into the
db.  Doesn't look like clojure/lein supports that out of the box but
looks like midje mights.  I'll have to put that on the list of things
to look at.

I'm annoyed I didn't add create and update dates to the todos.  Looks
like schema changes are coming up, maybe add them then.

I like how yesql is working so far but I'm not convinced I'm taking
advantage of all the bells and whistles.  Feels a little clunky
wrapping the db layer for things converting the UUID's.  There's
probably a better way.

### Step 5

Added dummy data using repl.

### Step 6

The error handling is crude.  Entering an invalid doneness state fails
an assert before getting into the db but the stack trace way more
spectacular than it needs to be.

I'm still unhappy about the amount of hiccup in routes.home.  That
probably ought to live in the views.


## License

Copyright © 2016 FIXME
