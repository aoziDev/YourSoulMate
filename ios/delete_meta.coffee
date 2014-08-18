fs = require 'fs'
metaFileName = '.DS_Store'
findMeta = (path) ->
  fs.readdir path, (err, files) ->
    for file in files
      do(file) ->
        pathWithFile = "#{path}/#{file}"
        if file is metaFileName
          fs.unlink pathWithFile (err) ->
            if err?
              console.log err
              return

            console.log "#{pathWithFile} was deleted"
          return

        fs.stat pathWithFile, (err, stats) ->
          if err?
            console.log err
            return

          if stats.isDirectory()
            findMeta pathWithFile

findMeta '.'
