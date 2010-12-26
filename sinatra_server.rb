require 'rubygems'
require 'sinatra'
require 'db'

DB.database_initialize('edict');

get '/' do
  "Usage: /edict/{romaji}"
end

get '/edict/*' do
    response ="" 
    @words = DB::Word.all :romaji => Regexp.new(params[:splat][0])
    if !@words.empty? then
      @words.each do |word|
      response += "#{word.kanji} #{word.hiragana} \n#{word.desc}<br>"
      end
    else
      "Not found"
    end
    response
end
