require "rubygems"
require "kakasi"
require "iconv"
require 'db'
# tail -n 2 /usr/share/edict/edict | kakasi -ieuc -Ha

class String
  def ujis
    Iconv.iconv("UJIS","UTF-8",self)
  end
  def utf8
    Iconv.iconv("UTF-8","UJIS",self)
  end
  def romaji
    Kakasi::kakasi("-ieuc -s -Ka -Ja -Ha", self)  #expect UJIS/EUC string
  end
end


class Importer
  def self.import(file_name)
      file = open(file_name)
      header =  file.readline
      file.each do |line|
          if /(.*)\[(.*)\][^\/]*[\/](.*)[\/]/.match(line) then
              kanji = $1
              hiragana = $2
              description = $3
              puts "k| #{kanji.utf8}| r| #{hiragana.romaji}| h| #{hiragana.utf8} description: #{$3}"
              DB.create_word(:kanji=>kanji.utf8, :hiragana => hiragana.utf8, :romaji => hiragana.romaji.utf8, :desc => $3.utf8)
          end
      end
  end
end

if __FILE__ == $0
  DB.database_initialize('edict')
  Importer::import("/usr/share/edict/edict")
end
