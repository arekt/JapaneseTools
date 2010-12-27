require 'rubygems'
require 'sinatra'
require 'db'
require 'erb'

DB.database_initialize('edict');

get '/' do
  "Usage: /edict/{romaji}"
end

get '/edict/*' do
    response ="" 
    case params[:splat][0].slice!(/\.xml/)
    when '.xml' then
      @words = DB::Word.all :romaji => Regexp.new(params[:splat][0]), :limit => 20
      template = ERB.new <<-EOF
<?xml version="1.0" encoding="UTF-8" ?>
<catalog>
  <% @words.each do |word| %>
  <title>
    <name><%=word.kanji%></name>
    <description><%= word.hiragana %>: <%= word.desc %></description>
  </title>
  <% end %>
</catalog>
EOF
      response=template.result(binding)
      puts template.result(binding) 
    else 
      @words = DB::Word.all :romaji => Regexp.new(params[:splat][0]), :limit => 20
      @words.each do |word|
        response += "#{word.kanji} #{word.hiragana} \n#{word.desc}<br>"
      end
    end
    response
end
