require "rubygems"
require "kakasi"
# tail -n 2 /usr/share/edict/edict | kakasi -ieuc -Ha


class Importer
  def self.romaji(h)
      Kakasi::kakasi("-ieuc -s -Ka -Ja -Ha", h)
  end

  def self.import(file_name)
      file = open(file_name)
      header =  file.readline
      file.each do |line|
          if /\[(.*)\]/.match(line) then
              hiragana = $1
              puts romaji(hiragana)
          end
      end
  end
end

if __FILE__ == $0
  Importer::import("/usr/share/edict/edict")
end
