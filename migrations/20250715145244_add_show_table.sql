-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Show] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Title] NVARCHAR(100) NOT NULL,
    [Description] NVARCHAR(1000) NOT NULL,
    [ReleaseDate] DATE NOT NULL,
    [Thumbnail] VARCHAR(MAX),
    [CreatedTime] DATETIME NOT NULL,
    [UpdatedTime] DATETIME NOT NULL,
    [OnGoing] BIT NOT NULL,
    [AgeRating] VARCHAR(50) NOT NULL,
    [IsSeries] BIT NOT NULL,
    [IsDeleted] BIT DEFAULT 0,
    [CommentSection] UNIQUEIDENTIFIER UNIQUE FOREIGN KEY REFERENCES [dbo].[CommentSection]([Id]),
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Show];
-- +goose StatementEnd
