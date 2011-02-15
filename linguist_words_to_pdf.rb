require 'rubygems'
require 'prawn'
require "prawn/measurement_extensions"
require 'linguist_words'

unit_name = ARGV[0]

pdf = Prawn::Document.new(:page_size => [200, 300])
pdf.font_families.update(
    "JapaneseFont" => {:normal => "/var/lib/defoma/gs.d/dirs/fonts/sazanami-mincho.ttf"})
pdf.font("JapaneseFont", :style => :normal)
#pdf.text "会社", :size => 46
#pdf.start_new_page
#pdf.text "ありがとう", :size => 16


DB.database_initialize('linguist2_development')
unit = Unit.first(:name=>"#{unit_name}")
Word.all(:unit_id =>unit.id).each do |w|
puts w.content
pdf.text w.content, :size => 16
end

pdf.render_file "#{unit_name}.pdf"
