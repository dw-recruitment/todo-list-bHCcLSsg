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

## License

Copyright © 2016 FIXME
